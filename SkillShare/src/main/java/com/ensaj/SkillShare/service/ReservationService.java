package com.ensaj.SkillShare.service;

import com.ensaj.SkillShare.model.*;
import com.ensaj.SkillShare.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import com.ensaj.SkillShare.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ServiceRepository serviceProposeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Reservation reserverService(int idServicePropose, int idCreateur, LocalDateTime dateHeure) throws Exception {
        Optional<ServicePropose> serviceOpt = serviceProposeRepository.findById(idServicePropose);
        if (serviceOpt.isEmpty()) {
            throw new Exception("Le service proposé avec l'ID " + idServicePropose + " n'existe pas.");
        }
        ServicePropose service = serviceOpt.get();

        Optional<Utilisateur> createurOpt = utilisateurRepository.findById(idCreateur);
        if (createurOpt.isEmpty()) {
            throw new Exception("Le créateur avec l'ID " + idCreateur + " n'existe pas.");
        }
        Utilisateur createur = createurOpt.get();

        Utilisateur destinataire = service.getUtilisateur();
        if (destinataire == null) {
            throw new Exception("Aucun destinataire associé au service proposé.");
        }

        Reservation reservation = new Reservation();
        reservation.setService(service);
        reservation.setCreateur(createur);
        reservation.setDestinataire(destinataire);
        reservation.setDateHeure(dateHeure); 
        reservation.setStatut("0"); 
        
        Reservation savedReservation = reservationRepository.save(reservation);

        Notification notification = new Notification();
        notification.setContenue("You have a new reservation for the service: " + service.getNomService() + " on " + reservation.getDateHeure() + " by " + createur.getNom() + " " + createur.getPrenom() + ".");
        notification.setDateEnvoie(LocalDateTime.now());
        notification.setStatut(false); 
        notification.setCreateur(createur);
        notification.setDestinataire(destinataire);

        notificationRepository.save(notification);

        return savedReservation;
    }
    
    public boolean hasUserReservationForService(int userId, int serviceId) {
        return reservationRepository.existsReservationByUserAndService(userId, serviceId);
    }
    
    
    @Transactional
    public void accepterReservation(int idReser) {
        Reservation reservation = reservationRepository.findById(idReser)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

    	Notification notification = new Notification();
        notification.setContenue("Votre réservation pour le service : " + reservation.getService().getNomService() + " est accepté");
        notification.setDateEnvoie(LocalDateTime.now());
        notification.setStatut(false); 
        notification.setCreateur(reservation.getDestinataire());
        notification.setDestinataire(reservation.getCreateur());
        notificationRepository.save(notification);
        
        reservationRepository.accepter(idReser);
    }

    @Transactional
    public void refuserReservation(int idReser) {
        Reservation reservation = reservationRepository.findById(idReser)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

    	Notification notification = new Notification();
        notification.setContenue("Votre réservation pour le service : " + reservation.getService().getNomService() +" est refusé");
        notification.setDateEnvoie(LocalDateTime.now());
        notification.setStatut(false); 
        notification.setCreateur(reservation.getDestinataire());
        notification.setDestinataire(reservation.getCreateur());
        notificationRepository.save(notification);
        
        reservationRepository.refuser(idReser);
    }
    
    public List<Map<String, Object>> getReservationsForUser(String token) throws Exception {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); 
        } else {
            throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
        }

        int idUser = jwtUtil.extractUserId(token);
        
        List<Object[]> results = reservationRepository.findReservationNonLu(idUser);
        List<Map<String, Object>> reservations = new ArrayList<>();

        for (Object[] row : results) {
            reservations.add(Map.of(
                    "idreser", row[0],
                    "date", row[1],
                    "nomService", row[2],
                    "nom", row[3],
                    "prenom", row[4],
                    "image", row[5]
            ));
        }
        return reservations;
    }

}

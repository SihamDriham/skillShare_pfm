package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.model.*;
import com.ensaj.SkillShare.service.*;
import com.ensaj.SkillShare.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/reserver/{idServicePropose}")
    public ResponseEntity<String> reserverService(
            @RequestHeader("Authorization") String token,
            @PathVariable int idServicePropose,@RequestBody LocalDateTime dateHeure) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); 
            } else {
                throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
            }

            int idCreateur = jwtUtil.extractUserId(token);

            reservationService.reserverService(idServicePropose, idCreateur, dateHeure);

            return ResponseEntity.ok("Réservation effectuée avec succès.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Map<String, Object>>> getReservations(
            @RequestHeader("Authorization") String token) {
        try {
            List<Map<String, Object>> reservations = reservationService.getReservationsForUser(token);

            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    
    @GetMapping("/check/{idServicePropose}")
    public ResponseEntity<Boolean> checkReservation(
    		@RequestHeader("Authorization") String token,
    		@PathVariable int idServicePropose) {
    	
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); 
        } else {
            throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
        }

        int idCreateur = jwtUtil.extractUserId(token);
        
        boolean hasReservation = reservationService.hasUserReservationForService(idCreateur, idServicePropose);
        return ResponseEntity.ok(hasReservation);
    }
    
    
    @PutMapping("/accepter/{idReser}")
    public ResponseEntity<Void> accepterReservation(@PathVariable int idReser) {
        reservationService.accepterReservation(idReser);
        return ResponseEntity.noContent().build(); 
    }

    @PutMapping("/refuser/{idReser}")
    public ResponseEntity<Void> refuserReservation(@PathVariable int idReser) {
        reservationService.refuserReservation(idReser);
        return ResponseEntity.noContent().build(); 
    }
    
}


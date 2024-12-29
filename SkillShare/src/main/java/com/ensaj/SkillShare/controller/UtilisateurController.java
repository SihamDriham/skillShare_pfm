package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.dto.AuthRequest;
import com.ensaj.SkillShare.dto.AuthResponse;
import com.ensaj.SkillShare.model.Utilisateur;
import com.ensaj.SkillShare.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        AuthResponse token = utilisateurService.authenticate(authRequest);
        return utilisateurService.findUserByEmailAndPassword(authRequest.getEmail(), authRequest.getMotDePasse())
                .map(user -> ResponseEntity.ok(token))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid email or password")));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable int id) {
        Optional<Utilisateur> utilisateur = utilisateurService.findUserById(id);
        if (utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody Utilisateur utilisateur) {
        try {
            if (utilisateur.getImage() != null && !utilisateur.getImage().isEmpty()) {
                String imageSource = utilisateur.getImage();
                String fileName = UUID.randomUUID().toString() + ".png";
                Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
                Files.createDirectories(targetPath.getParent());
                Path sourcePath = Paths.get(imageSource).normalize();
                if (!Files.exists(sourcePath) || !Files.isReadable(sourcePath)) {
                    return ResponseEntity.badRequest().body("Chemin d'image local invalide ou introuvable.");
                }
                Files.copy(sourcePath, targetPath);
                utilisateur.setImage("/images/" + fileName);
            }else {
                utilisateur.setImage("/images/personIcon.png");
            }
            Utilisateur savedUser = utilisateurService.addUser(utilisateur);
            return ResponseEntity.ok("Utilisateur ajouté avec succès. Chemin de l'image : " + savedUser.getImage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du traitement ou de l'enregistrement de l'image.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'ajout de l'utilisateur.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody Utilisateur utilisateur) {
        try {
            Optional<Utilisateur> existingUser = utilisateurService.findUserById(id);
            if (existingUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Utilisateur introuvable avec l'ID : " + id);
            }
            Utilisateur userUpdated = existingUser.get();
            userUpdated.setNom(utilisateur.getNom());
            userUpdated.setPrenom(utilisateur.getPrenom());
            userUpdated.setEmail(utilisateur.getEmail());
            userUpdated.setMotDePasse(utilisateur.getMotDePasse());

            System.out.println("imagePath: " + utilisateur.getImage());
            if (utilisateur.getImage() != null && !utilisateur.getImage().isEmpty()) {
                String imagePath = utilisateur.getImage();
                System.out.println("imagePath1: " + imagePath);
                if (imagePath.startsWith("/images/")) {
                    userUpdated.setImage(imagePath);
                } else {
                    String fileName = UUID.randomUUID().toString() + ".png";
                    Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
                    Files.createDirectories(targetPath.getParent());

                    Files.copy(new ByteArrayInputStream(Base64.getDecoder().decode(imagePath)), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("imagePath2: " + fileName);
                    userUpdated.setImage("/images/" + fileName);
                }
            } else {
                userUpdated.setImage(existingUser.get().getImage());
            }
            utilisateurService.updateUser(userUpdated);  
            System.out.println("User updated with image: " + userUpdated.getImage());

            return ResponseEntity.ok("Utilisateur mis à jour avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }


    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }

            String fileName = UUID.randomUUID().toString() + ".png";
            Path targetPath = Paths.get("src/main/resources/static/images/").resolve(fileName).normalize();
            Files.createDirectories(targetPath.getParent());

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Image uploaded successfully: " + fileName);
            return ResponseEntity.ok("/images/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du téléchargement de l'image : " + e.getMessage());
        }
    }



}

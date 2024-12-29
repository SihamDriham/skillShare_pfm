package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.model.Feedback;
import com.ensaj.SkillShare.service.*;
import com.ensaj.SkillShare.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedBackController {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private JwtUtil jwtUtil; 

    @PostMapping("/add/{idServicePropose}")
    public ResponseEntity<String> addFeedback(
            @RequestHeader("Authorization") String token, 
            @PathVariable int idServicePropose, 
            @RequestBody Feedback feedbackRequest 
    ) {
        try {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); 
        } else {
            throw new IllegalArgumentException("Le token doit commencer par 'Bearer '");
        }
         int idCreateur = jwtUtil.extractUserId(token);
         
         feedbackService.addFeedback(
                 feedbackRequest.getNote(),
                 feedbackRequest.getCommentaire(),
                 idServicePropose,
                 idCreateur
         );
         return ResponseEntity.ok("Ajout avec succes ");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/feedbacks/{idServicePropose}")
    public ResponseEntity<List<Map<String, Object>>> getFeedbacksForService(@PathVariable int idServicePropose) {
        try {
        	List<Map<String, Object>> feedbacks = feedbackService.getFeedbacksForService(idServicePropose);
            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(feedbacks);
            }
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
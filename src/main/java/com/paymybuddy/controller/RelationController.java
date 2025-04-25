package com.paymybuddy.controller;

import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.List;

@RestController
public class RelationController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RelationService relationService;

    // Route pour afficher la liste des relations de l'utilisateur connecté
    @GetMapping("/user/relations")
    public ResponseEntity<?> getUserRelations(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        try {
            if (jwtToken.isEmpty()) {
                return ResponseEntity.status(401).body(new ApiResponse("Aucun token trouvé", null));
            }

            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            String username = claims.getSubject();

            // Récupérer les relations de l'utilisateur
            List<String> relations = relationService.getUserRelations(username);

            if (relations.isEmpty()) {
                return ResponseEntity.ok().body(new ApiResponse("Aucune relation trouvée", null));
            }

            return ResponseEntity.ok().body(new ApiResponse("Relations récupérées avec succès", relations));

        } catch (JwtException e) {
            return ResponseEntity.status(401).body(new ApiResponse("Token invalide ou expiré", null));
        }
    }



    // Route pour ajouter une relation à l'utilisateur connecté via l'email de la relation
    @PostMapping("/user/relation/add")
    public ResponseEntity<?> addRelation(@RequestHeader("Authorization") String authorizationHeader,
                                         @RequestBody AddRelationRequest request) {
        try {
            String token = authorizationHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
            String currentUserEmail = claims.getSubject();

            boolean success = relationService.addRelation(currentUserEmail, request.getRelationEmail());

            if (success) {
                return ResponseEntity.ok().body(new ApiResponse("Relation added successfully", null));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse("Failed to add relation", null));
            }

        } catch (JwtException e) {
            return ResponseEntity.status(401).body(new ApiResponse("Token invalide ou expiré", null));
        }
    }

    public static class AddRelationRequest {
        private String relationEmail;

        public String getRelationEmail() {
            return relationEmail;
        }

        public void setRelationEmail(String relationEmail) {
            this.relationEmail = relationEmail;
        }
    }

    public static class ApiResponse {
        private String message;
        private Object data;

        public ApiResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}

package com.paymybuddy.controller;

import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.service.RelationService;  // Service pour gérer les relations
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.List;

@RestController
public class RelationController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RelationService relationService;  // Service pour gérer les relations

    /**
     * Route pour afficher la liste des relations de l'utilisateur connecté
     * Cette route récupère les relations de l'utilisateur à partir du service
     */
    @GetMapping("/user/relations")
    public ResponseEntity<?> getUserRelations(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extraire le token et récupérer les informations de l'utilisateur
            String token = authorizationHeader.substring(7); // "Bearer " est enlevé
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
            String username = claims.getSubject();

            // Récupérer les relations de l'utilisateur depuis le service
            List<String> relations = relationService.getUserRelations(username);

            // Retourner les relations sous forme de réponse JSON
            return ResponseEntity.ok().body(new ApiResponse("Relations fetched successfully", relations));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(new ApiResponse("Token invalide ou expiré", null));
        }
    }

    /**
     * Route pour ajouter une relation à l'utilisateur connecté via l'email de la relation
     * Accessible uniquement avec un token JWT valide
     */
    @PostMapping("/user/relations/add")
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

    // Classe pour recevoir les données du body JSON (email de la relation)
    public static class AddRelationRequest {
        private String relationEmail;

        public String getRelationEmail() {
            return relationEmail;
        }

        public void setRelationEmail(String relationEmail) {
            this.relationEmail = relationEmail;
        }
    }

    // Classe pour structurer la réponse JSON
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

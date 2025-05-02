package com.paymybuddy.controller;

import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.service.RelationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les relations entre utilisateurs.
 */
@Controller
public class RelationController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RelationService relationService;

    /**
     * Affiche la liste des relations de l'utilisateur connecté.
     *
     * @param jwtToken Le token JWT envoyé par le client
     * @return La réponse contenant les relations de l'utilisateur ou un message d'erreur
     */
    @GetMapping("/user-relations")
    public ResponseEntity<?> getUserRelations(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        try {
            if (jwtToken.isEmpty()) {
                return ResponseEntity.status(401).body(new ApiResponse("Aucun token trouvé", null));
            }

            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            String username = claims.getSubject();

            List<String> relations = relationService.getUserRelations(username);

            return ResponseEntity.ok().body(new ApiResponse("Relations récupérées avec succès", relations));

        } catch (JwtException e) {
            return ResponseEntity.status(401).body(new ApiResponse("Token invalide ou expiré", null));
        }
    }

    /**
     * Ajoute une relation à l'utilisateur connecté via l'email de la relation.
     *
     * @param jwtToken      Le token JWT de l'utilisateur
     * @param relationEmail L'email de la relation à ajouter
     * @param model         Le modèle pour afficher les messages dans la vue
     * @return La vue de la page des relations avec un message de succès ou d'erreur
     */
    @PostMapping("/user/relation/add")
    public String addRelation(@CookieValue(value = "JWT", defaultValue = "") String jwtToken,
                              @RequestParam("email") String relationEmail,
                              Model model) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";
        }

        try {
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            String currentUserEmail = claims.getSubject();

            boolean success = relationService.addRelation(currentUserEmail, relationEmail);

            if (success) {
                model.addAttribute("message", "Relation ajoutée avec succès !");
            } else {
                model.addAttribute("message", "Échec de l'ajout de la relation (elle existe peut-être déjà).");
            }

            return "relations";

        } catch (JwtException e) {
            return "redirect:/connexion";
        } catch (Exception e) {
            model.addAttribute("message", "Une erreur est survenue : " + e.getMessage());
            return "relations";
        }
    }

    /**
     * Structure de réponse API contenant un message et des données.
     */
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

    /**
     * Représente une requête d'ajout de relation.
     */
    public static class AddRelationRequest {
        private String relationEmail;

        public String getRelationEmail() {
            return relationEmail;
        }

        public void setRelationEmail(String relationEmail) {
            this.relationEmail = relationEmail;
        }
    }
}

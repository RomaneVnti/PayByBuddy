package com.paymybuddy.controller;

import com.paymybuddy.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@RestController  // Utiliser RestController pour une réponse JSON
public class HomeController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Route pour afficher la page d'inscription
     * Cette route n'a pas besoin d'un token JWT car c'est une page d'inscription publique
     */
    @GetMapping("/inscription")
    public ResponseEntity<?> showInscriptionPage() {
        // Message JSON indiquant que le formulaire d'inscription peut être rempli
        String inscriptionForm = "Formulaire d'inscription: Veuillez fournir vos informations pour vous inscrire.";

        // Retourner un objet JSON avec les informations d'inscription
        return ResponseEntity.ok().body(new ApiResponse("Inscription", inscriptionForm));
    }

    /**
     * Route pour afficher la page de connection
     * Cette route n'a pas besoin d'un token JWT car c'est une page de connection publique
     */
    @GetMapping("/connection")
    public ResponseEntity<?> showConnexionPage() {
        // Message JSON indiquant que le formulaire de connexion peut être rempli
        String connexionForm = "Formulaire de connection: Veuillez entrer vos identifiants pour vous connecter.";

        // Retourner un objet JSON avec les informations de connexion
        return ResponseEntity.ok().body(new ApiResponse("Connection", connexionForm));
    }

    /**
     * Route pour afficher la page d'accueil (transactions)
     * Cette route vérifie d'abord si l'utilisateur est authentifié via un token JWT
     */
    @GetMapping("/home")
    public ResponseEntity<?> showHomePage(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Récupérer le token de l'en-tête Authorization (format "Bearer <token>")
            String token = authorizationHeader.substring(7); // "Bearer " est enlevé

            // Extraire les informations du token
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);

            // Vérifier le nom d'utilisateur (par exemple l'email)
            String username = claims.getSubject();

            // Exemple de données pour les transactions
            String transactionsInfo = "Transactions de " + username + ": Transfert 1, Transfert 2, Transfert 3";

            // Retourner un objet JSON avec les informations de transaction
            return ResponseEntity.ok().body(new ApiResponse("Success", transactionsInfo));

        } catch (JwtException e) {
            // Si le token est invalide ou expiré, renvoyer une erreur JSON
            return ResponseEntity.status(401).body(new ApiResponse("Token invalide ou expiré", null));
        }
    }

    /**
     * Route pour afficher la page des relations
     * Cette route vérifie d'abord si l'utilisateur est authentifié via un token JWT
     */
    @GetMapping("/relations")
    public ResponseEntity<?> showRelationsPage(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Récupérer le token de l'en-tête Authorization (format "Bearer <token>")
            String token = authorizationHeader.substring(7); // "Bearer " est enlevé

            // Extraire les informations du token
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);

            // Vérifier le nom d'utilisateur (par exemple l'email)
            String username = claims.getSubject();

            // Exemple de données pour les relations
            String relationsInfo = "Relations de " + username + ": ami1, ami2, ami3";

            // Retourner un objet JSON avec les relations
            return ResponseEntity.ok().body(new ApiResponse("Success", relationsInfo));

        } catch (JwtException e) {
            // Si le token est invalide ou expiré, renvoyer une erreur JSON
            return ResponseEntity.status(401).body(new ApiResponse("Token invalide ou expiré", null));
        }
    }

    /**
     * Route pour afficher la page des transferts
     * Cette route vérifie d'abord si l'utilisateur est authentifié via un token JWT
     */
    @GetMapping("/transfert")
    public ResponseEntity<?> showTransfertPage(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Récupérer le token de l'en-tête Authorization (format "Bearer <token>")
            String token = authorizationHeader.substring(7); // "Bearer " est enlevé

            // Extraire les informations du token
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);

            // Vérifier le nom d'utilisateur (par exemple l'email)
            String username = claims.getSubject();

            // Exemple de données pour les transferts
            String transfertInfo = "Transferts de " + username + ": Transfert 100€, Transfert 200€, Transfert 300€";

            // Retourner un objet JSON avec les transferts
            return ResponseEntity.ok().body(new ApiResponse("Success", transfertInfo));

        } catch (JwtException e) {
            // Si le token est invalide ou expiré, renvoyer une erreur JSON
            return ResponseEntity.status(401).body(new ApiResponse("Token invalide ou expiré", null));
        }
    }

    /**
     * Route pour afficher la page du profil
     * Cette route vérifie d'abord si l'utilisateur est authentifié via un token JWT
     */
    @GetMapping("/profil")
    public ResponseEntity<?> showProfilPage(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Récupérer le token de l'en-tête Authorization (format "Bearer <token>")
            String token = authorizationHeader.substring(7); // "Bearer " est enlevé

            // Extraire les informations du token
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);

            // Vérifier le nom d'utilisateur (par exemple l'email)
            String username = claims.getSubject();

            // Exemple de données pour le profil
            String profilInfo = "Profil de " + username + ": Nom: " + username + ", Email: " + username + "@example.com";

            // Retourner un objet JSON avec les informations du profil
            return ResponseEntity.ok().body(new ApiResponse("Success", profilInfo));

        } catch (JwtException e) {
            // Si le token est invalide ou expiré, renvoyer une erreur JSON
            return ResponseEntity.status(401).body(new ApiResponse("Token invalide ou expiré", null));
        }
    }

    // Classe pour structurer la réponse JSON
    public static class ApiResponse {
        private String message;
        private String data;

        public ApiResponse(String message, String data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public String getData() {
            return data;
        }
    }
}

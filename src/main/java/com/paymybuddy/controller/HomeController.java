package com.paymybuddy.controller;

import com.paymybuddy.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Controller
public class HomeController {


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Route pour afficher la page d'inscription
     * Cette route n'a pas besoin d'un token JWT car c'est une page d'inscription publique
     */
    @GetMapping("/inscription")
    public String showInscriptionPage() {
        // Si tu veux afficher la page d'inscription, tu renvoies la vue Thymeleaf
        return "inscription";  // Assure-toi d'avoir un fichier inscription.html dans le dossier /src/main/resources/templates/
    }

    /**
     * Route pour afficher la page de connection
     * Cette route n'a pas besoin d'un token JWT car c'est une page de connection publique
     */
    @GetMapping("/connection")
    public String showConnectionPage() {
        return "connection";  // Assure-toi que le fichier connection.html existe dans /src/main/resources/templates/
    }

    /**
     * Route pour afficher la page d'accueil (transactions)
     * Cette route vérifie d'abord si l'utilisateur est authentifié via un token JWT
     */
    @GetMapping("/home")
    public String showHomePage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connection";  // Si pas de token, rediriger vers la page de connexion
        }

        try {
            // Valider et extraire les informations du token JWT
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);

            // Utiliser les informations du token pour afficher la page d'accueil
            String username = claims.getSubject();
            return "home";  // Renvoie la vue home.html
        } catch (JwtException e) {
            // Si le token est invalide ou expiré, redirige vers la connexion
            return "redirect:/connection";
        }
    }



    /**
     * Route pour afficher la page des relations
     * Cette route vérifie d'abord si l'utilisateur est authentifié via un token JWT
     */
    @GetMapping("/relations")
    public String showRelationPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connection";  // Si pas de token, rediriger vers la page de connexion
        }

        try {
            // Valider et extraire les informations du token JWT
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);

            // Utiliser les informations du token pour afficher la page d'accueil
            String username = claims.getSubject();
            return "relations";  // Renvoie la vue home.html
        } catch (JwtException e) {
            // Si le token est invalide ou expiré, redirige vers la connexion
            return "redirect:/connection";
        }
    }

    /**
     * Route pour afficher la page des transferts
     * Cette route vérifie d'abord si l'utilisateur est authentifié via un token JWT
     */
    @GetMapping("/transfer")
    public String showTransferPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connection";  // Si pas de token, rediriger vers la page de connexion
        }

        try {
            // Valider et extraire les informations du token JWT
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);

            // Utiliser les informations du token pour afficher la page d'accueil
            String username = claims.getSubject();
            return "transfer";  // Renvoie la vue home.html
        } catch (JwtException e) {
            // Si le token est invalide ou expiré, redirige vers la connexion
            return "redirect:/connection";
        }
    }

    /**
     * Route pour afficher la page du profil
     * Cette route vérifie d'abord si l'utilisateur est authentifié via un token JWT
     */
    @GetMapping("/profil")
    public String showProfilPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connection";  // Si pas de token, rediriger vers la page de connexion
        }

        try {
            // Valider et extraire les informations du token JWT
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);

            // Utiliser les informations du token pour afficher la page d'accueil
            String username = claims.getSubject();
            return "profil";  // Renvoie la vue home.html
        } catch (JwtException e) {
            // Si le token est invalide ou expiré, redirige vers la connexion
            return "redirect:/connection";
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

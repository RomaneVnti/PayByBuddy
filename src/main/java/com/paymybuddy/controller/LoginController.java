package com.paymybuddy.controller;

import com.paymybuddy.dto.LoginDTO;
import com.paymybuddy.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer l'authentification des utilisateurs
 */

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * Endpoint pour l'authentification d'un utilisateur.
     *
     * @param email Contient l'email et le mot de passe de l'utilisateur
     * @return Le token JWT généré si l'utilisateur est authentifié avec succès
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response) {
        // Authentification de l'utilisateur
        String token = loginService.authenticate(email, password);

        // Créer un cookie avec le token JWT
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(false);  // Sécuriser le cookie pour qu'il ne soit pas accessible par JavaScript
        cookie.setSecure(false);   // Utilise false en HTTP (si tu utilises HTTP, sinon mettre true pour HTTPS)
        cookie.setPath("/");       // Le cookie est accessible pour toute l'application
        cookie.setMaxAge(3600);    // Le cookie expirera après 1 heure

        // Ajouter le cookie à la réponse HTTP
        response.addCookie(cookie);

        // Rediriger vers la page d'accueil après la connexion
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/home").build();
    }



}

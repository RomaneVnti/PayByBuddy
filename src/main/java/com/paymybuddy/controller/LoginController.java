package com.paymybuddy.controller;

import com.paymybuddy.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer l'authentification des utilisateurs.
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * Endpoint pour l'authentification d'un utilisateur.
     * Cette méthode authentifie l'utilisateur avec l'email et le mot de passe fournis,
     * puis génère un token JWT et l'envoie dans un cookie.
     *
     * @param email    L'email de l'utilisateur
     * @param password Le mot de passe de l'utilisateur
     * @param response L'objet HttpServletResponse pour ajouter le cookie à la réponse
     * @return Une réponse HTTP avec le statut FOUND (302) pour rediriger vers la page d'accueil
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      HttpServletResponse response) {
        // Authentifier l'utilisateur et récupérer le token JWT
        String token = loginService.authenticate(email, password);

        // Créer un cookie pour stocker le token JWT
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(false);  // Le cookie ne sera pas accessible par JavaScript (peut-être pour faciliter les tests)
        cookie.setSecure(false);    // Utiliser false pour HTTP, à modifier en true pour HTTPS
        cookie.setPath("/");        // Le cookie est accessible pour toute l'application
        cookie.setMaxAge(3600);     // Le cookie expirera après 1 heure

        // Ajouter le cookie à la réponse HTTP
        response.addCookie(cookie);

        // Rediriger vers la page d'accueil après la connexion réussie
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/home").build();
    }
}

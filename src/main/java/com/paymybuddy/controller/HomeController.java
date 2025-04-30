package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour gérer l'affichage des pages du site (inscription, connexion, accueil, relations, transferts, profil)
 */
@Controller
public class HomeController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    /**
     * Route pour afficher la page d'inscription.
     * Cette route est publique, aucun token JWT n'est nécessaire.
     *
     * @return La vue d'inscription
     */
    @GetMapping("/inscription")
    public String showInscriptionPage() {
        return "inscription";  // La vue Thymeleaf pour l'inscription
    }

    /**
     * Route pour afficher la page de connexion.
     * Cette route est publique, aucun token JWT n'est nécessaire.
     *
     * @return La vue de connexion
     */
    @GetMapping("/connexion")
    public String showConnexionPage() {
        return "connexion";  // La vue Thymeleaf pour la connexion
    }

    /**
     * Route pour afficher la page d'accueil (transactions).
     * Cette route nécessite un token JWT pour vérifier l'authentification de l'utilisateur.
     *
     * @param jwtToken Le token JWT dans les cookies de l'utilisateur
     * @return La vue d'accueil ou redirige vers la page de connexion si le token est invalide
     */
    @GetMapping("/home")
    public String showHomePage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";  // Si pas de token, redirige vers la page de connexion
        }

        try {
            // Valider et extraire les informations du token JWT
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            return "home";  // La vue Thymeleaf pour la page d'accueil
        } catch (JwtException e) {
            return "redirect:/connexion";  // Redirige si le token est invalide ou expiré
        }
    }

    /**
     * Route pour afficher la page des relations.
     * Cette route nécessite un token JWT pour vérifier l'authentification de l'utilisateur.
     *
     * @param jwtToken Le token JWT dans les cookies de l'utilisateur
     * @return La vue des relations ou redirige vers la page de connexion si le token est invalide
     */
    @GetMapping("/relations")
    public String showRelationPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";  // Si pas de token, redirige vers la page de connexion
        }

        try {
            // Valider et extraire les informations du token JWT
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            return "relations";  // La vue Thymeleaf pour la page des relations
        } catch (JwtException e) {
            return "redirect:/connexion";  // Redirige si le token est invalide ou expiré
        }
    }

    /**
     * Route pour afficher la page des transferts.
     * Cette route nécessite un token JWT pour vérifier l'authentification de l'utilisateur.
     *
     * @param jwtToken Le token JWT dans les cookies de l'utilisateur
     * @return La vue des transferts ou redirige vers la page de connexion si le token est invalide
     */
    @GetMapping("/transfer")
    public String showTransferPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";  // Si pas de token, redirige vers la page de connexion
        }

        try {
            // Valider et extraire les informations du token JWT
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            return "transfer";  // La vue Thymeleaf pour la page des transferts
        } catch (JwtException e) {
            return "redirect:/connexion";  // Redirige si le token est invalide ou expiré
        }
    }

    /**
     * Route pour afficher la page du profil.
     * Cette route nécessite un token JWT pour vérifier l'authentification de l'utilisateur.
     *
     * @param jwtToken Le token JWT dans les cookies de l'utilisateur
     * @param model Le modèle Thymeleaf pour passer les données à la vue
     * @return La vue du profil ou redirige vers la page de connexion si le token est invalide
     */
    @GetMapping("/profil")
    public String showProfilPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken, Model model) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";  // Si pas de token, redirige vers la page de connexion
        }

        try {
            // Valider et extraire les informations du token JWT
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            String email = claims.getSubject();  // Récupère l'email de l'utilisateur connecté

            // Récupérer l'utilisateur depuis le service en utilisant l'email
            User user = userService.findUserByEmail(email);

            // Ajouter les données de l'utilisateur au modèle
            model.addAttribute("user", user);  // Passer l'objet User à la vue

            return "profil";  // La vue Thymeleaf pour le profil
        } catch (JwtException e) {
            return "redirect:/connexion";  // Redirige si le token est invalide ou expiré
        }
    }

}

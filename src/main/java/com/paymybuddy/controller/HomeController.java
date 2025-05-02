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
 * Contrôleur principal gérant les routes des pages de l'application Web.
 *
 * Les routes protégées nécessitent la présence d'un token JWT valide dans les cookies.
 * En cas d'absence ou d'invalidité du token, l'utilisateur est redirigé vers la page de connexion.
 */
@Controller
public class HomeController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    /**
     * Affiche la page d'inscription.
     *
     * @return le nom de la vue "inscription"
     */
    @GetMapping("/inscription")
    public String showInscriptionPage() {
        return "inscription";
    }

    /**
     * Affiche la page de connexion.
     *
     * @return le nom de la vue "connexion"
     */
    @GetMapping("/connexion")
    public String showConnexionPage() {
        return "connexion";
    }

    /**
     * Affiche la page d'accueil (home) si l'utilisateur possède un token JWT valide.
     *
     * @param jwtToken le token JWT récupéré depuis les cookies
     * @return le nom de la vue "home" ou une redirection vers "/connexion" si le token est absent ou invalide
     */
    @GetMapping("/home")
    public String showHomePage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";
        }

        try {
            jwtTokenProvider.getClaimsFromToken(jwtToken);
            return "home";
        } catch (JwtException e) {
            return "redirect:/connexion";
        }
    }

    /**
     * Affiche la page des relations de l'utilisateur si le token JWT est valide.
     *
     * @param jwtToken le token JWT récupéré depuis les cookies
     * @return le nom de la vue "relations" ou une redirection vers "/connexion" si le token est absent ou invalide
     */
    @GetMapping("/relations")
    public String showRelationPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";
        }

        try {
            jwtTokenProvider.getClaimsFromToken(jwtToken);
            return "relations";
        } catch (JwtException e) {
            return "redirect:/connexion";
        }
    }

    /**
     * Affiche la page de transferts si le token JWT est valide.
     *
     * @param jwtToken le token JWT récupéré depuis les cookies
     * @return le nom de la vue "transfer" ou une redirection vers "/connexion" si le token est absent ou invalide
     */
    @GetMapping("/transfer")
    public String showTransferPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";
        }

        try {
            jwtTokenProvider.getClaimsFromToken(jwtToken);
            return "transfer";
        } catch (JwtException e) {
            return "redirect:/connexion";
        }
    }

    /**
     * Affiche la page du profil de l'utilisateur si le token JWT est valide.
     * Les informations de l'utilisateur sont ajoutées au modèle.
     *
     * @param jwtToken le token JWT récupéré depuis les cookies
     * @param model le modèle utilisé pour injecter les données dans la vue
     * @return le nom de la vue "profil" ou une redirection vers "/connexion" si le token est absent ou invalide
     */
    @GetMapping("/profil")
    public String showProfilPage(@CookieValue(value = "JWT", defaultValue = "") String jwtToken, Model model) {
        if (jwtToken.isEmpty()) {
            return "redirect:/connexion";
        }

        try {
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwtToken);
            String email = claims.getSubject();
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
            return "profil";
        } catch (JwtException e) {
            return "redirect:/connexion";
        }
    }
}

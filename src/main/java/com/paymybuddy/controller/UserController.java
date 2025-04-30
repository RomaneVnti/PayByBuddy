package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.service.LoginService;
import com.paymybuddy.service.UserService;
import com.paymybuddy.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import io.jsonwebtoken.JwtException;
import com.paymybuddy.security.JwtTokenProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Contrôleur REST qui gère les opérations liées aux utilisateurs.
 * Expose les endpoints pour la création et la gestion des utilisateurs.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private LoginService loginService;

    /**
     * Endpoint pour créer un utilisateur et l'authentifier immédiatement.
     * Cette méthode crée un utilisateur, puis génère un token JWT pour l'authentifier.
     * Le token est ensuite stocké dans un cookie HTTP.
     *
     * @param username Le nom d'utilisateur à créer.
     * @param email L'email de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @param response La réponse HTTP permettant d'ajouter le cookie avec le token JWT.
     * @return La page d'accueil après la création et l'authentification de l'utilisateur.
     */
    @PostMapping("/create")
    public String createUser(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             HttpServletResponse response) {
        // Création de l'utilisateur
        userService.createUser(username, email, password);

        // Authentifier l'utilisateur nouvellement créé
        String token = loginService.authenticate(email, password);

        // Créer un cookie avec le token JWT
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(false);  // Sécuriser le cookie pour qu'il ne soit pas accessible par JavaScript
        cookie.setSecure(false);    // Utilise false en HTTP (mettre true pour HTTPS)
        cookie.setPath("/");        // Le cookie est accessible pour toute l'application
        cookie.setMaxAge(3600);     // Le cookie expirera après 1 heure
        response.addCookie(cookie);

        // Rediriger vers la page d'accueil après la création et l'authentification
        return "redirect:/home"; // Redirection vers la page d'accueil
    }

    /**
     * Endpoint pour mettre à jour le profil d'un utilisateur.
     * Cette méthode permet à un utilisateur de mettre à jour son profil (nom d'utilisateur, email, mot de passe).
     * Le token JWT de l'utilisateur est utilisé pour identifier l'utilisateur courant.
     *
     * @param authorizationHeader Le token JWT de l'utilisateur dans l'en-tête Authorization.
     * @param userDTO Le DTO contenant les informations à jour pour l'utilisateur.
     * @return ResponseEntity contenant l'utilisateur mis à jour ou un message d'erreur en cas de problème.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody UserDTO userDTO) {

        try {
            // Extraire le token JWT de l'en-tête Authorization
            String token = authorizationHeader.substring(7);
            String currentUserEmail = jwtTokenProvider.getClaimsFromToken(token).getSubject();

            // Mettre à jour le profil de l'utilisateur en utilisant le service UserService
            User updatedUser = userService.updateUser(currentUserEmail, userDTO);

            // Retourner la réponse avec l'utilisateur mis à jour et le code HTTP OK (200)
            return ResponseEntity.ok(updatedUser);

        } catch (JwtException e) {
            // Si le token est invalide ou expiré, retourner une erreur 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou expiré");
        } catch (Exception e) {
            // Si une autre erreur survient, retourner une erreur 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la mise à jour du profil");
        }
    }

    /**
     * Endpoint pour récupérer le profil d'un utilisateur.
     * Cette méthode permet de récupérer les informations du profil de l'utilisateur actuellement authentifié.
     *
     * @param authorizationHeader Le token JWT de l'utilisateur dans l'en-tête Authorization.
     * @return ResponseEntity contenant les informations de l'utilisateur ou un message d'erreur en cas de problème.
     */
    @GetMapping("/user/profil")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extraire le token JWT de l'en-tête Authorization
            String token = authorizationHeader.substring(7);
            String currentUserEmail = jwtTokenProvider.getClaimsFromToken(token).getSubject();

            // Récupérer l'utilisateur correspondant à l'email extrait du token
            User user = userService.findUserByEmail(currentUserEmail);

            // Retourner l'utilisateur dans la réponse HTTP
            return ResponseEntity.ok(user);

        } catch (JwtException e) {
            // Si le token est invalide ou expiré, retourner une erreur 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            // Si une autre erreur survient, retourner une erreur 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

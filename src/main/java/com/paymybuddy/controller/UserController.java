package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import com.paymybuddy.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import com.paymybuddy.security.JwtTokenProvider;

/**
 * Contrôleur REST qui gère les opérations liées aux utilisateurs.
 * Expose les endpoints pour la création et la gestion des utilisateurs.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Endpoint pour créer un utilisateur.
     * Cette méthode permet de créer un utilisateur en envoyant un DTO contenant le nom d'utilisateur, l'email et le mot de passe.
     *
     * @param userDTO Le DTO contenant les informations de l'utilisateur (nom d'utilisateur, email, mot de passe).
     * @return ResponseEntity contenant l'utilisateur créé avec un code de statut CREATED.
     */
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        // Créer l'utilisateur en utilisant le service UserService
        User createdUser = userService.createUser(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword()
        );
        // Retourner la réponse avec l'utilisateur créé et le code HTTP CREATED (201)
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
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
}

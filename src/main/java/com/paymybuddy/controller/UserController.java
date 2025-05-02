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
 * Contrôleur REST pour les opérations liées aux utilisateurs.
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
     * Crée un utilisateur, l'authentifie immédiatement et stocke le token JWT dans un cookie.
     */
    @PostMapping("/create")
    public String createUser(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             HttpServletResponse response) {
        userService.createUser(username, email, password);
        String token = loginService.authenticate(email, password);

        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return "redirect:/home";
    }

    /**
     * Met à jour le profil de l'utilisateur connecté.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody UserDTO userDTO) {
        try {
            String token = authorizationHeader.substring(7);
            String currentUserEmail = jwtTokenProvider.getClaimsFromToken(token).getSubject();

            User updatedUser = userService.updateUser(currentUserEmail, userDTO);
            return ResponseEntity.ok(updatedUser);

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou expiré");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la mise à jour du profil");
        }
    }

    /**
     * Récupère le profil de l'utilisateur connecté.
     */
    @GetMapping("/user/profil")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            String currentUserEmail = jwtTokenProvider.getClaimsFromToken(token).getSubject();

            User user = userService.findUserByEmail(currentUserEmail);
            return ResponseEntity.ok(user);

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

package com.paymybuddy.controller;

import com.paymybuddy.dto.LoginDTO;
import com.paymybuddy.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer l'authentification des utilisateurs
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * Endpoint pour l'authentification d'un utilisateur.
     *
     * @param loginDTO Contient l'email et le mot de passe de l'utilisateur
     * @return Le token JWT généré si l'utilisateur est authentifié avec succès
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        // Authentifier l'utilisateur et obtenir le token
        String token = loginService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());

        // Retourne le token JWT si l'authentification est réussie
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}

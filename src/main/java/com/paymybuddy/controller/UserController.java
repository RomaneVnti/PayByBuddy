package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint pour créer un utilisateur
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            // Appel au service pour créer l'utilisateur
            User createdUser = userService.createUser(user.getUsername(), user.getEmail(), user.getPassword());

            // Retourner l'utilisateur créé avec un statut HTTP 201 (Created)
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // En cas d'erreur, retourner un message d'erreur avec le code 400 (Bad Request)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
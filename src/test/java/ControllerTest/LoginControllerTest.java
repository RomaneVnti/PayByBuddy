package com.paymybuddy.controller;

import com.paymybuddy.controller.LoginController;
import com.paymybuddy.dto.LoginDTO;
import com.paymybuddy.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitaire pour le contrôleur {@link LoginController}.
 * Utilise Mockito pour simuler les dépendances et tester les comportements du contrôleur.
 */
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private LoginDTO loginDTO;

    private HttpServletResponse response;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     */
    @BeforeEach
    void setUp() {
        loginDTO = new LoginDTO("john.doe@example.com", "password123");
        response = mock(HttpServletResponse.class);
    }

    /**
     * Test pour la connexion réussie d'un utilisateur.
     * Vérifie que le contrôleur retourne un code HTTP 302 et ajoute un cookie avec le token JWT.
     */
    @Test
    void login_ShouldReturnRedirectAndSetCookie_WhenCredentialsAreValid() {
        String token = "valid-jwt-token";
        when(loginService.authenticate(loginDTO.getEmail(), loginDTO.getPassword())).thenReturn(token);

        ResponseEntity<Void> responseEntity = loginController.login(loginDTO.getEmail(), loginDTO.getPassword(), response);

        // Vérifie que la réponse a un statut HTTP 302 FOUND
        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());

        // Vérifie que le redirectionnement vers "/home" a bien eu lieu
        assertEquals("/home", responseEntity.getHeaders().getLocation().getPath());

        // Vérifie que le cookie avec le token JWT a bien été ajouté à la réponse
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        verify(response).addCookie(cookie);
    }

    /**
     * Test pour la connexion échouée d'un utilisateur avec des identifiants incorrects.
     * Vérifie que le contrôleur gère correctement l'exception d'authentification.
     */
    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        when(loginService.authenticate(loginDTO.getEmail(), loginDTO.getPassword()))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        // Test de la méthode pour les credentials incorrects
        try {
            loginController.login(loginDTO.getEmail(), loginDTO.getPassword(), response);
            fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            // Vérifie que l'exception est bien lancée
            assertEquals("Invalid credentials", e.getMessage());
        }
    }

    /**
     * Test pour la connexion échouée d'un utilisateur avec un email invalide.
     * Vérifie que le contrôleur gère correctement une exception spécifique.
     */
    @Test
    void login_ShouldReturnBadRequest_WhenEmailIsInvalid() {
        // Simuler l'échec de l'authentification avec un email invalide
        String invalidEmail = "invalid-email@example.com";
        when(loginService.authenticate(invalidEmail, loginDTO.getPassword()))
                .thenThrow(new IllegalArgumentException("Email not found"));

        try {
            loginController.login(invalidEmail, loginDTO.getPassword(), response);
            fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            // Vérifie que l'exception a été lancée pour email invalide
            assertEquals("Email not found", e.getMessage());
        }
    }

    /**
     * Test pour la connexion échouée d'un utilisateur avec un mot de passe incorrect.
     * Vérifie que le contrôleur gère correctement une exception d'authentification.
     */
    @Test
    void login_ShouldReturnBadRequest_WhenPasswordIsIncorrect() {
        // Simuler l'échec de l'authentification avec un mot de passe incorrect
        when(loginService.authenticate(loginDTO.getEmail(), loginDTO.getPassword()))
                .thenThrow(new IllegalArgumentException("Incorrect password"));

        try {
            loginController.login(loginDTO.getEmail(), loginDTO.getPassword(), response);
            fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            // Vérifie que l'exception a été lancée pour mot de passe incorrect
            assertEquals("Incorrect password", e.getMessage());
        }
    }
}

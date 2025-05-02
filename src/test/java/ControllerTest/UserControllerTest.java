package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.model.User;
import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.service.UserService;
import com.paymybuddy.service.LoginService;
import com.paymybuddy.exception.EmailAlreadyExistsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
 * Test unitaire pour le contrôleur {@link UserController}.
 * Utilise Mockito pour simuler les dépendances et tester les comportements du contrôleur.
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private UserController userController;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Claims claims;

    @Mock
    private HttpServletResponse response;
    private UserDTO userDTO;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     */
    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("johnDoe", "john.doe@example.com", "password123");  // Initialisation du DTO
    }

    /**
     * Test pour la création d'un utilisateur avec succès.
     * Vérifie que le contrôleur retourne un code HTTP 201 (Created) et l'utilisateur créé.
     */
    @Test
    void createUser_Success() {
        String username = "johnDoe";
        String email = "john.doe@example.com";
        String password = "password123";

        // Mock du comportement du service
        when(userService.createUser(username, email, password)).thenReturn(null); // Aucune action spécifique

        // Appel de la méthode du contrôleur
        String result = userController.createUser(username, email, password, response);

        // Vérifier que la redirection vers la page d'accueil a bien eu lieu
        assertEquals("redirect:/home", result);
    }

    /**
     * Test pour la mise à jour du profil d'un utilisateur avec succès.
     */
    @Test
    void updateUserProfile_Success() {
        String validToken = "valid.jwt.token";
        String currentUserEmail = "john.doe@example.com";

        User updatedUser = new User();
        updatedUser.setUsername("johnDoeUpdated");
        updatedUser.setEmail(currentUserEmail);

        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(userService.updateUser(eq(currentUserEmail), eq(userDTO))).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUserProfile("Bearer " + validToken, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof User);
        assertEquals("johnDoeUpdated", ((User) response.getBody()).getUsername());
    }

    /**
     * Test pour la mise à jour du profil échouant en cas de token invalide.
     */
    @Test
    void updateUserProfile_InvalidToken() {
        String invalidToken = "invalid.token";

        when(jwtTokenProvider.getClaimsFromToken(invalidToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = userController.updateUserProfile("Bearer " + invalidToken, userDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token invalide ou expiré", response.getBody());
    }

    /**
     * Test pour la mise à jour du profil échouant en cas d'erreur générale.
     */
    @Test
    void updateUserProfile_GeneralError() {
        String validToken = "valid.jwt.token";
        String currentUserEmail = "john.doe@example.com";

        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(userService.updateUser(eq(currentUserEmail), eq(userDTO)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = userController.updateUserProfile("Bearer " + validToken, userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erreur lors de la mise à jour du profil", response.getBody());
    }

    /**
     * Test pour la récupération du profil de l'utilisateur.
     * Vérifie que le contrôleur retourne un code HTTP 200 (OK) avec l'utilisateur récupéré.
     */
    @Test
    void getUserProfile_Success() {
        String validToken = "valid.jwt.token";
        String currentUserEmail = "john.doe@example.com";

        User user = new User();
        user.setEmail(currentUserEmail);
        user.setUsername("johnDoe");

        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(userService.findUserByEmail(currentUserEmail)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserProfile("Bearer " + validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(currentUserEmail, response.getBody().getEmail());
    }

    /**
     * Test pour la récupération du profil d'un utilisateur échouant en cas de token invalide.
     */
    @Test
    void getUserProfile_InvalidToken() {
        String invalidToken = "invalid.token";

        when(jwtTokenProvider.getClaimsFromToken(invalidToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<User> response = userController.getUserProfile("Bearer " + invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Test pour la récupération du profil d'un utilisateur échouant en cas d'erreur générale.
     */
    @Test
    void getUserProfile_GeneralError() {
        String validToken = "valid.jwt.token";
        String currentUserEmail = "john.doe@example.com";

        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(userService.findUserByEmail(currentUserEmail)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<User> response = userController.getUserProfile("Bearer " + validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}

package ControllerTest;

import com.paymybuddy.controller.UserController;
import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.model.User;
import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.service.UserService;
import com.paymybuddy.exception.EmailAlreadyExistsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
    private UserService userService;  // Mock de UserService

    @InjectMocks
    private UserController userController;  // Injecte les mocks dans le contrôleur

    @Mock
    private JwtTokenProvider jwtTokenProvider; // Ajoute ce mock pour extraire les claims JWT

    @Mock
    private Claims claims;


    private UserDTO userDTO;  // Objet DTO représentant un utilisateur pour les tests



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


    /**
     * Test pour la création d'un utilisateur échouant en cas d'email déjà existant.
     * Vérifie que le contrôleur gère correctement l'exception EmailAlreadyExistsException.
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

    @Test
    void updateUserProfile_InvalidToken() {
        String invalidToken = "invalid.token";

        when(jwtTokenProvider.getClaimsFromToken(invalidToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = userController.updateUserProfile("Bearer " + invalidToken, userDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token invalide ou expiré", response.getBody());
    }

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
}

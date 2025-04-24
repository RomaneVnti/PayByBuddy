package ControllerTest;

import com.paymybuddy.controller.LoginController;
import com.paymybuddy.dto.LoginDTO;
import com.paymybuddy.service.LoginService;
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
 * Test unitaire pour le contrôleur {@link LoginController}.
 * Utilise Mockito pour simuler les dépendances et tester les comportements du contrôleur.
 */
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;  // Mock du service de login

    @InjectMocks
    private LoginController loginController;  // Injection de la dépendance dans le contrôleur

    private LoginDTO loginDTO;  // Objet DTO représentant les informations de connexion

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     */
    @BeforeEach
    void setUp() {
        // Initialisation du DTO de login avec un email et un mot de passe pour les tests
        loginDTO = new LoginDTO("john.doe@example.com", "password123");
    }

    /**
     * Test pour la connexion réussie d'un utilisateur.
     * Vérifie que le contrôleur retourne un code HTTP 200 et un token JWT.
     */
    @Test
    void login_Success() {
        // Création d'un token JWT simulé
        String token = "jwtToken123";

        // Stubbing de la méthode authenticate du service pour retourner un token
        when(loginService.authenticate(any(), any())).thenReturn(token);

        // Appel à la méthode du contrôleur
        ResponseEntity<String> response = loginController.login(loginDTO);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Vérification du code HTTP
        assertNotNull(response.getBody());  // Vérification que la réponse contient un token
        assertEquals(token, response.getBody());  // Vérification que le token retourné est correct
    }

    /**
     * Test pour la connexion échouée d'un utilisateur avec des identifiants incorrects.
     * Vérifie que le contrôleur gère correctement l'exception d'authentification.
     */
    @Test
    void login_Failure_InvalidCredentials() {
        // Stubbing de la méthode authenticate du service pour lancer une exception de type JwtException
        when(loginService.authenticate(any(), any())).thenThrow(new JwtException("Invalid email or password"));

        // Test de l'exception
        try {
            loginController.login(loginDTO);  // Appel de la méthode du contrôleur
            fail("Expected JwtException to be thrown");  // Si l'exception n'est pas levée, échoue le test
        } catch (JwtException e) {
            // Vérification que l'exception est bien levée et que le message est correct
            assertEquals("Invalid email or password", e.getMessage());
        }
    }

    /**
     * Test pour la connexion échouée d'un utilisateur avec un email invalide.
     * Vérifie que le contrôleur gère correctement une exception spécifique.
     */
    @Test
    void login_Failure_EmailNotFound() {
        // Stubbing de la méthode authenticate du service pour lancer une exception d'email introuvable
        when(loginService.authenticate(any(), any())).thenThrow(new JwtException("Email not found"));

        // Test de l'exception
        try {
            loginController.login(loginDTO);  // Appel de la méthode du contrôleur
            fail("Expected JwtException to be thrown");
        } catch (JwtException e) {
            // Vérification que le message d'exception est correct
            assertEquals("Email not found", e.getMessage());
        }
    }
}

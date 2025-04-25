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


    /**
     * Test pour la connexion échouée d'un utilisateur avec des identifiants incorrects.
     * Vérifie que le contrôleur gère correctement l'exception d'authentification.
     */


    /**
     * Test pour la connexion échouée d'un utilisateur avec un email invalide.
     * Vérifie que le contrôleur gère correctement une exception spécifique.
     */

}

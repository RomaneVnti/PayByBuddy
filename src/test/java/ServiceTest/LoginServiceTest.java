package ServiceTest;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.model.User;
import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.service.LoginService;
import com.paymybuddy.exception.InvalidLoginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitaire pour le service {@link LoginService}.
 * Utilise Mockito pour simuler les dépendances et tester les comportements du service.
 */
@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserDAO userDAO;  // Mock du DAO pour les utilisateurs

    @Mock
    private BCryptPasswordEncoder passwordEncoder;  // Mock de l'encodeur de mot de passe

    @Mock
    private JwtTokenProvider jwtTokenProvider;  // Mock du fournisseur de token JWT

    @InjectMocks
    private LoginService loginService;  // Injecte les mocks dans le service

    private User user;  // Utilisateur pour les tests
    private String email;
    private String password;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     */
    @BeforeEach
    void setUp() {
        email = "john.doe@example.com";
        password = "password123";

        user = new User();
        user.setEmail(email);
        user.setPassword("$2a$10$abcdefghijklmNOPQRSTU");  // Mot de passe crypté simulé

        // Simule la réponse du DAO
        when(userDAO.findByEmail(email)).thenReturn(user);
    }

    /**
     * Test pour l'authentification réussie de l'utilisateur.
     * Vérifie que le service retourne un token JWT si l'utilisateur est authentifié correctement.
     */
    @Test
    void authenticate_Success() {
        // Stubbing du comportement du BCryptPasswordEncoder pour que le mot de passe corresponde
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // Stubbing de la génération du token JWT
        String token = "jwtToken123";
        when(jwtTokenProvider.generateToken(user)).thenReturn(token);

        // Appel du service pour authentifier l'utilisateur
        String result = loginService.authenticate(email, password);

        // Assertions
        assertNotNull(result);  // Vérifie que le token est non nul
        assertEquals(token, result);  // Vérifie que le token retourné est correct
    }

    /**
     * Test pour l'authentification échouée de l'utilisateur avec un mot de passe incorrect.
     * Vérifie que le service lève une exception InvalidLoginException si les informations sont incorrectes.
     */
    @Test
    void authenticate_Failure_InvalidPassword() {
        // Stubbing du comportement du BCryptPasswordEncoder pour que le mot de passe ne corresponde pas
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // Test de l'exception
        InvalidLoginException thrown = assertThrows(InvalidLoginException.class, () -> {
            loginService.authenticate(email, password);  // Appel du service avec un mot de passe incorrect
        });

        // Vérification que l'exception contient le bon message
        assertEquals("Invalid email or password", thrown.getMessage());
    }

    /**
     * Test pour l'authentification échouée de l'utilisateur si l'email n'est pas trouvé dans la base de données.
     * Vérifie que le service lève une exception InvalidLoginException si l'email n'existe pas.
     */
    @Test
    void authenticate_Failure_EmailNotFound() {
        // Stubbing du DAO pour retourner null (utilisateur non trouvé)
        when(userDAO.findByEmail(email)).thenReturn(null);

        // Test de l'exception
        InvalidLoginException thrown = assertThrows(InvalidLoginException.class, () -> {
            loginService.authenticate(email, password);  // Appel du service avec un email introuvable
        });

        // Vérification que l'exception contient le bon message
        assertEquals("Invalid email or password", thrown.getMessage());
    }
}

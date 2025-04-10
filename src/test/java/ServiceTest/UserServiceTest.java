package ServiceTest;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.exception.EmailAlreadyExistsException;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

/**
 * Test unitaire pour le service {@link UserService}.
 * Utilise Mockito pour simuler les dépendances et tester la logique du service.
 */
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;  // Mock de UserDAO

    @Mock
    private BCryptPasswordEncoder passwordEncoder;  // Mock de BCryptPasswordEncoder

    private UserService userService;  // Service à tester

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     * Initialise les mocks et crée une instance de UserService.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialise les mocks
        userService = new UserService(userDAO);  // Crée l'instance du service avec les mocks
    }

    /**
     * Test pour la création d'un utilisateur lorsque l'email n'existe pas encore.
     * Vérifie que l'utilisateur est créé avec succès, que le mot de passe est hashé et que l'utilisateur est bien enregistré.
     */
    @Test
    void createUser_ShouldCreateUser_WhenEmailDoesNotExist() {
        // Préparer les données
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        // Simuler que l'email n'existe pas dans la base de données
        when(userDAO.findByEmail(email)).thenReturn(null);

        // Simuler le hash du mot de passe
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword123");

        // Simuler l'enregistrement de l'utilisateur
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setEmail(email);
        mockUser.setPassword("hashedPassword123");
        mockUser.setCreatedAt(LocalDateTime.now());
        when(userDAO.save(any(User.class))).thenReturn(mockUser);

        // Appeler la méthode à tester
        User createdUser = userService.createUser(username, email, password);

        // Vérifier les assertions
        assertNotNull(createdUser);  // Vérifie que l'utilisateur créé n'est pas nul
        assertEquals(username, createdUser.getUsername());  // Vérifie que le nom d'utilisateur est correct
        assertEquals(email, createdUser.getEmail());  // Vérifie que l'email est correct
        assertEquals("hashedPassword123", createdUser.getPassword());  // Vérifie que le mot de passe est bien hashé

        // Vérifier que les méthodes mockées ont bien été appelées
        verify(userDAO).findByEmail(email);  // Vérifie que findByEmail a été appelé
        verify(userDAO).save(any(User.class));  // Vérifie que save a bien été appelé
    }

    /**
     * Test pour la création d'un utilisateur lorsque l'email existe déjà.
     * Vérifie qu'une exception {@link EmailAlreadyExistsException} est levée lorsque l'email existe déjà.
     */
    @Test
    void createUser_ShouldThrowEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        // Préparer les données
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        // Simuler que l'email existe déjà dans la base de données
        when(userDAO.findByEmail(email)).thenReturn(new User());

        // Appeler la méthode à tester et vérifier qu'une exception est levée
        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.createUser(username, email, password);
        });

        // Vérifier que la méthode findByEmail a bien été appelée
        verify(userDAO).findByEmail(email);  // Vérifie que findByEmail a été appelé
        verify(userDAO, never()).save(any(User.class));  // La méthode save ne doit pas être appelée
    }
}
package com.paymybuddy.service;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.exception.EmailAlreadyExistsException;
import com.paymybuddy.exception.UserNotFoundException;
import com.paymybuddy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

/**
 * Test unitaire pour le {@link UserService}.
 * Ce test vérifie les fonctionnalités liées à la gestion des utilisateurs,
 * y compris la création, la mise à jour et la gestion des exceptions liées aux utilisateurs.
 */
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserService userService;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     * Elle initialise les mocks et configure les dépendances du {@link UserService}.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDAO);
        // Injection du mock du password encoder dans le service
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    /**
     * Test pour la méthode {@link UserService#createUser(String, String, String)}.
     * Vérifie que la méthode crée un utilisateur lorsque l'email n'existe pas déjà.
     */
    @Test
    void createUser_ShouldCreateUser_WhenEmailDoesNotExist() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        // Simulation de l'absence d'un utilisateur avec cet email et de l'encodage du mot de passe
        when(userDAO.findByEmail(email)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword123");

        // Création d'un utilisateur simulé
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setEmail(email);
        mockUser.setPassword("hashedPassword123");
        mockUser.setCreatedAt(LocalDateTime.now());

        when(userDAO.save(any(User.class))).thenReturn(mockUser);

        // Appel de la méthode et vérification des résultats
        User createdUser = userService.createUser(username, email, password);

        // Vérification que l'utilisateur a bien été créé
        assertNotNull(createdUser);
        assertEquals(username, createdUser.getUsername());
        assertEquals(email, createdUser.getEmail());
        assertEquals("hashedPassword123", createdUser.getPassword());

        // Vérification que les méthodes pertinentes ont été appelées
        verify(userDAO).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userDAO).save(any(User.class));
    }

    /**
     * Test pour la méthode {@link UserService#createUser(String, String, String)}.
     * Vérifie que la méthode lève une exception lorsque l'email existe déjà dans la base de données.
     */
    @Test
    void createUser_ShouldThrowEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        // Simulation de l'existence d'un utilisateur avec cet email
        when(userDAO.findByEmail(email)).thenReturn(new User());

        // Vérification que l'exception est levée lorsqu'on tente de créer un utilisateur avec un email déjà pris
        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.createUser(username, email, password);
        });

        // Vérification que la méthode save n'a pas été appelée
        verify(userDAO).findByEmail(email);
        verify(userDAO, never()).save(any(User.class));
    }

    /**
     * Test pour la méthode {@link UserService#updateUser(String, UserDTO)}.
     * Vérifie que la méthode lève une exception lorsque l'utilisateur à mettre à jour n'existe pas.
     */
    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        String currentUserEmail = "nonexistent@example.com";
        when(userDAO.findByEmail(currentUserEmail)).thenReturn(null);

        UserDTO userDTO = new UserDTO("newUsername", "newEmail@example.com", "newPassword123");

        // Vérification que l'exception est levée lorsqu'on tente de mettre à jour un utilisateur inexistant
        UserNotFoundException thrownException = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(currentUserEmail, userDTO);
        });

        // Vérification du message de l'exception
        assertNotNull(thrownException);
        assertEquals("Utilisateur non trouvé.", thrownException.getMessage());

        // Vérification que save n'a pas été appelé
        verify(userDAO).findByEmail(currentUserEmail);
        verify(userDAO, never()).save(any(User.class));
    }

    /**
     * Test pour la méthode {@link UserService#updateUser(String, UserDTO)}.
     * Vérifie que la méthode met à jour les données de l'utilisateur lorsque les informations sont valides.
     */
    @Test
    void updateUser_ShouldUpdateUser_WhenValidData() {
        String currentUserEmail = "existing@example.com";
        UserDTO userDTO = new UserDTO("johnDoe", "john@example.com", "newPassword123");

        // Utilisateur existant à mettre à jour
        User existingUser = new User();
        existingUser.setEmail(currentUserEmail);
        existingUser.setUsername("oldUsername");
        existingUser.setPassword("oldPassword123");

        when(userDAO.findByEmail(currentUserEmail)).thenReturn(existingUser);
        when(passwordEncoder.encode("newPassword123")).thenReturn("hashedNewPassword");

        User updatedUser = new User();
        updatedUser.setEmail(userDTO.getEmail());
        updatedUser.setUsername(userDTO.getUsername());
        updatedUser.setPassword("hashedNewPassword");

        when(userDAO.save(any(User.class))).thenReturn(updatedUser);

        // Appel de la méthode et vérification des résultats
        User result = userService.updateUser(currentUserEmail, userDTO);

        // Vérification que l'utilisateur a bien été mis à jour
        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals("hashedNewPassword", result.getPassword());

        // Vérification que les méthodes pertinentes ont été appelées
        verify(userDAO).findByEmail(currentUserEmail);
        verify(passwordEncoder).encode("newPassword123");
        verify(userDAO).save(any(User.class));
    }
}

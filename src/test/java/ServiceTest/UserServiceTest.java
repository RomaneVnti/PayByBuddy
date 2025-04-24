package ServiceTest;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.exception.EmailAlreadyExistsException;
import com.paymybuddy.exception.UserNotFoundException;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDAO);
        // Injection du mock dans le champ privé final
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void createUser_ShouldCreateUser_WhenEmailDoesNotExist() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        when(userDAO.findByEmail(email)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword123");

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setEmail(email);
        mockUser.setPassword("hashedPassword123");
        mockUser.setCreatedAt(LocalDateTime.now());

        when(userDAO.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser(username, email, password);

        assertNotNull(createdUser);
        assertEquals(username, createdUser.getUsername());
        assertEquals(email, createdUser.getEmail());
        assertEquals("hashedPassword123", createdUser.getPassword());

        verify(userDAO).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userDAO).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        when(userDAO.findByEmail(email)).thenReturn(new User());

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.createUser(username, email, password);
        });

        verify(userDAO).findByEmail(email);
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        String currentUserEmail = "nonexistent@example.com";
        when(userDAO.findByEmail(currentUserEmail)).thenReturn(null);

        UserDTO userDTO = new UserDTO("newUsername", "newEmail@example.com", "newPassword123");

        UserNotFoundException thrownException = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(currentUserEmail, userDTO);
        });

        assertNotNull(thrownException);
        assertEquals("User not found.", thrownException.getMessage());

        verify(userDAO).findByEmail(currentUserEmail);
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        // currentUserEmail est l'utilisateur actuellement connecté
        String currentUserEmail = "original@example.com";

        // DTO contient un nouvel email, qui est déjà pris
        UserDTO userDTO = new UserDTO("newUsername", "taken@example.com", "newPassword123");

        // Simuler que l'utilisateur actuel existe
        User existingUser = new User();
        existingUser.setEmail(currentUserEmail);
        when(userDAO.findByEmail(currentUserEmail)).thenReturn(existingUser);

        // Simuler qu'un autre utilisateur a déjà l'email "taken@example.com"
        User otherUser = new User();
        otherUser.setEmail("taken@example.com");
        when(userDAO.findByEmail(userDTO.getEmail())).thenReturn(otherUser);

        // Appeler la méthode à tester et vérifier l'exception
        EmailAlreadyExistsException thrownException = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.updateUser(currentUserEmail, userDTO);
        });

        assertNotNull(thrownException);
        assertEquals("The email is already in use.", thrownException.getMessage());

        verify(userDAO).findByEmail(currentUserEmail);
        verify(userDAO).findByEmail(userDTO.getEmail());
        verify(userDAO, never()).save(any(User.class));
    }


    @Test
    void updateUser_ShouldUpdateUser_WhenValidData() {
        String currentUserEmail = "existing@example.com";
        UserDTO userDTO = new UserDTO("johnDoe", "john@example.com", "newPassword123");

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

        User result = userService.updateUser(currentUserEmail, userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals("hashedNewPassword", result.getPassword());

        verify(userDAO).findByEmail(currentUserEmail);
        verify(passwordEncoder).encode("newPassword123");
        verify(userDAO).save(any(User.class));
    }
}

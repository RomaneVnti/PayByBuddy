package ServiceTest;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.dao.UserRelationsDAO;
import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.SelfRelationException;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserRelations;
import com.paymybuddy.service.RelationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RelationServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserRelationsDAO userRelationsDAO;

    private RelationService relationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        relationService = new RelationService();
        // Injecter les mocks manuellement
        ReflectionTestUtils.setField(relationService, "userDAO", userDAO);
        ReflectionTestUtils.setField(relationService, "userRelationsDAO", userRelationsDAO);
    }

    @Test
    void getUserRelations_ShouldReturnEmails_WhenUserExists() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("user@example.com");

        User relationUser = new User();
        relationUser.setUserId(2);
        relationUser.setEmail("friend@example.com");

        UserRelations relation = new UserRelations();
        relation.setUser1(user);
        relation.setUser2(relationUser);

        when(userDAO.findByEmail("user@example.com")).thenReturn(user);
        when(userRelationsDAO.getUserRelations(1)).thenReturn(List.of(relation));

        List<String> result = relationService.getUserRelations("user@example.com");

        assertEquals(1, result.size());
        assertEquals("friend@example.com", result.get(0));
    }

    @Test
    void getUserRelations_ShouldThrowException_WhenUserNotFound() {
        when(userDAO.findByEmail("unknown@example.com")).thenReturn(null);

        assertThrows(EmailNotFoundException.class, () -> {
            relationService.getUserRelations("unknown@example.com");
        });
    }

    @Test
    void addRelation_ShouldThrowSelfRelationException_WhenEmailsAreSame() {
        assertThrows(SelfRelationException.class, () -> {
            relationService.addRelation("same@example.com", "same@example.com");
        });
    }

    @Test
    void addRelation_ShouldThrowException_WhenRelationEmailNotFound() {
        when(userDAO.findByEmail("friend@example.com")).thenReturn(null);

        assertThrows(EmailNotFoundException.class, () -> {
            relationService.addRelation("user@example.com", "friend@example.com");
        });
    }

    @Test
    void addRelation_ShouldThrowException_WhenUserEmailNotFound() {
        User relationUser = new User();
        relationUser.setUserId(2);
        relationUser.setEmail("friend@example.com");

        when(userDAO.findByEmail("friend@example.com")).thenReturn(relationUser);
        when(userDAO.findByEmail("user@example.com")).thenReturn(null);

        assertThrows(EmailNotFoundException.class, () -> {
            relationService.addRelation("user@example.com", "friend@example.com");
        });
    }

    @Test
    void addRelation_ShouldThrowException_WhenRelationAlreadyExists() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("user@example.com");

        User relationUser = new User();
        relationUser.setUserId(2);
        relationUser.setEmail("friend@example.com");

        when(userDAO.findByEmail("user@example.com")).thenReturn(user);
        when(userDAO.findByEmail("friend@example.com")).thenReturn(relationUser);
        when(userRelationsDAO.findRelationByIds(1, 2)).thenReturn(new UserRelations());

        assertThrows(RuntimeException.class, () -> {
            relationService.addRelation("user@example.com", "friend@example.com");
        });
    }

    @Test
    void addRelation_ShouldAddRelation_WhenValidData() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("user@example.com");

        User relationUser = new User();
        relationUser.setUserId(2);
        relationUser.setEmail("friend@example.com");

        when(userDAO.findByEmail("user@example.com")).thenReturn(user);
        when(userDAO.findByEmail("friend@example.com")).thenReturn(relationUser);
        when(userRelationsDAO.findRelationByIds(1, 2)).thenReturn(null);

        boolean result = relationService.addRelation("user@example.com", "friend@example.com");

        assertTrue(result);
        verify(userRelationsDAO).save(any(UserRelations.class));
    }
}

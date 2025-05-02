package com.paymybuddy.service;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.dao.UserRelationsDAO;
import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.SelfRelationException;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserRelations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test unitaire pour le {@link RelationService}.
 * Ce test vérifie le bon fonctionnement des méthodes liées à la gestion des relations utilisateurs.
 * Les tests couvrent la récupération des relations, l'ajout de relations, et la gestion des erreurs.
 */
public class RelationServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserRelationsDAO userRelationsDAO;

    private RelationService relationService;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     * Elle initialise les mocks et configure les dépendances du {@link RelationService}.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        relationService = new RelationService();
        // Injection des mocks dans le service
        ReflectionTestUtils.setField(relationService, "userDAO", userDAO);
        ReflectionTestUtils.setField(relationService, "userRelationsDAO", userRelationsDAO);
    }

    /**
     * Test pour la méthode {@link RelationService#getUserRelations(String)}.
     * Vérifie que la méthode retourne les emails des relations d'un utilisateur existant.
     */
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

        // Simuler les appels aux DAO
        when(userDAO.findByEmail("user@example.com")).thenReturn(user);
        when(userRelationsDAO.getUserRelations(1)).thenReturn(List.of(relation));

        List<String> result = relationService.getUserRelations("user@example.com");

        // Vérification du résultat
        assertEquals(1, result.size());
        assertEquals("friend@example.com", result.get(0));
    }

    /**
     * Test pour la méthode {@link RelationService#getUserRelations(String)}.
     * Vérifie que la méthode lève une exception lorsque l'utilisateur n'est pas trouvé.
     */
    @Test
    void getUserRelations_ShouldThrowException_WhenUserNotFound() {
        // Simuler un utilisateur inexistant
        when(userDAO.findByEmail("unknown@example.com")).thenReturn(null);

        // Vérification que l'exception est bien levée
        assertThrows(EmailNotFoundException.class, () -> {
            relationService.getUserRelations("unknown@example.com");
        });
    }

    /**
     * Test pour la méthode {@link RelationService#addRelation(String, String)}.
     * Vérifie que la méthode lève une exception lorsque l'utilisateur tente de se lier à lui-même.
     */
    @Test
    void addRelation_ShouldThrowSelfRelationException_WhenEmailsAreSame() {
        // Vérification que l'exception est levée pour une relation avec soi-même
        assertThrows(SelfRelationException.class, () -> {
            relationService.addRelation("same@example.com", "same@example.com");
        });
    }

    /**
     * Test pour la méthode {@link RelationService#addRelation(String, String)}.
     * Vérifie que la méthode lève une exception lorsque l'email de la relation est introuvable.
     */
    @Test
    void addRelation_ShouldThrowException_WhenRelationEmailNotFound() {
        // Simuler l'absence de l'utilisateur cible de la relation
        when(userDAO.findByEmail("friend@example.com")).thenReturn(null);

        // Vérification que l'exception est levée
        assertThrows(EmailNotFoundException.class, () -> {
            relationService.addRelation("user@example.com", "friend@example.com");
        });
    }

    /**
     * Test pour la méthode {@link RelationService#addRelation(String, String)}.
     * Vérifie que la méthode lève une exception lorsque l'email de l'utilisateur est introuvable.
     */
    @Test
    void addRelation_ShouldThrowException_WhenUserEmailNotFound() {
        User relationUser = new User();
        relationUser.setUserId(2);
        relationUser.setEmail("friend@example.com");

        // Simuler l'absence de l'utilisateur de la relation
        when(userDAO.findByEmail("friend@example.com")).thenReturn(relationUser);
        when(userDAO.findByEmail("user@example.com")).thenReturn(null);

        // Vérification que l'exception est levée
        assertThrows(EmailNotFoundException.class, () -> {
            relationService.addRelation("user@example.com", "friend@example.com");
        });
    }

    /**
     * Test pour la méthode {@link RelationService#addRelation(String, String)}.
     * Vérifie que la méthode lève une exception lorsque la relation existe déjà.
     */
    @Test
    void addRelation_ShouldThrowException_WhenRelationAlreadyExists() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("user@example.com");

        User relationUser = new User();
        relationUser.setUserId(2);
        relationUser.setEmail("friend@example.com");

        // Simuler la présence d'une relation existante
        when(userDAO.findByEmail("user@example.com")).thenReturn(user);
        when(userDAO.findByEmail("friend@example.com")).thenReturn(relationUser);
        when(userRelationsDAO.findRelationByIds(1, 2)).thenReturn(new UserRelations());

        // Vérification que l'exception est levée
        assertThrows(RuntimeException.class, () -> {
            relationService.addRelation("user@example.com", "friend@example.com");
        });
    }

    /**
     * Test pour la méthode {@link RelationService#addRelation(String, String)}.
     * Vérifie que la relation est ajoutée correctement lorsque les données sont valides.
     */
    @Test
    void addRelation_ShouldAddRelation_WhenValidData() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("user@example.com");

        User relationUser = new User();
        relationUser.setUserId(2);
        relationUser.setEmail("friend@example.com");

        // Simuler un cas où la relation n'existe pas encore
        when(userDAO.findByEmail("user@example.com")).thenReturn(user);
        when(userDAO.findByEmail("friend@example.com")).thenReturn(relationUser);
        when(userRelationsDAO.findRelationByIds(1, 2)).thenReturn(null);

        // Vérification que la relation est ajoutée
        boolean result = relationService.addRelation("user@example.com", "friend@example.com");
        assertTrue(result);

        // Vérification que l'élément a bien été sauvegardé
        verify(userRelationsDAO).save(any(UserRelations.class));
    }
}

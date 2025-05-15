package com.paymybuddy.service;

import com.paymybuddy.dao.TransactionDAO;
import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.dao.UserRelationsDAO;
import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.InvalidAmountException;
import com.paymybuddy.model.Transactions;
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
 * Test unitaire pour le {@link TransactionService}.
 * Ce test vérifie le bon fonctionnement des méthodes liées à la gestion des transactions entre utilisateurs.
 * Les tests couvrent l'ajout de transactions, la gestion des erreurs, et la récupération des transactions.
 */
public class TransactionServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TransactionDAO transactionDAO;

    @Mock
    private UserRelationsDAO userRelationsDAO;

    private TransactionService transactionService;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     * Elle initialise les mocks et configure les dépendances du {@link TransactionService}.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService();
        // Injection des mocks dans le service
        ReflectionTestUtils.setField(transactionService, "userDAO", userDAO);
        ReflectionTestUtils.setField(transactionService, "transactionDAO", transactionDAO);
        ReflectionTestUtils.setField(transactionService, "userRelationsDAO", userRelationsDAO);

    }

    /**
     * Test pour la méthode {@link TransactionService#addTransaction(String, String, String, double)}.
     * Vérifie que la méthode lève une exception lorsque le montant est nul ou négatif.
     */
    @Test
    void addTransaction_ShouldThrowInvalidAmountException_WhenAmountIsZeroOrNegative() {
        // Vérification que l'exception est levée pour un montant invalide (0 ou négatif)
        assertThrows(InvalidAmountException.class, () -> {
            transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", 0);
        });

        assertThrows(InvalidAmountException.class, () -> {
            transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", -10);
        });
    }

    /**
     * Test pour la méthode {@link TransactionService#addTransaction(String, String, String, double)}.
     * Vérifie que la méthode lève une exception lorsque l'email de l'expéditeur est introuvable.
     */
    @Test
    void addTransaction_ShouldThrowEmailNotFoundException_WhenSenderNotFound() {
        when(userDAO.findByEmail("sender@example.com")).thenReturn(null);

        // Vérification que l'exception est levée pour un expéditeur introuvable
        assertThrows(EmailNotFoundException.class, () -> {
            transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", 100);
        });
    }

    /**
     * Test pour la méthode {@link TransactionService#addTransaction(String, String, String, double)}.
     * Vérifie que la méthode lève une exception lorsque l'email du récepteur est introuvable.
     */
    @Test
    void addTransaction_ShouldThrowEmailNotFoundException_WhenReceiverNotFound() {
        User sender = new User();
        sender.setEmail("sender@example.com");

        when(userDAO.findByEmail("sender@example.com")).thenReturn(sender);
        when(userDAO.findByEmail("receiver@example.com")).thenReturn(null);

        // Vérification que l'exception est levée pour un récepteur introuvable
        assertThrows(EmailNotFoundException.class, () -> {
            transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", 100);
        });
    }

    /**
     * Test pour la méthode {@link TransactionService#addTransaction(String, String, String, double)}.
     * Vérifie que la méthode ajoute correctement une transaction valide.
     */
    @Test
    void addTransaction_ShouldAddTransaction_WhenValidData() {
        User sender = new User();
        sender.setUserId(1);
        sender.setEmail("sender@example.com");
        sender.setSolde(200); // Solde suffisant

        User receiver = new User();
        receiver.setUserId(2);
        receiver.setEmail("receiver@example.com");
        receiver.setSolde(50);

        when(userDAO.findByEmail("sender@example.com")).thenReturn(sender);
        when(userDAO.findByEmail("receiver@example.com")).thenReturn(receiver);
        when(userRelationsDAO.findRelationByIds(1, 2)).thenReturn(new UserRelations());

        Transactions result = transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", 100);

        verify(transactionDAO).save(any(Transactions.class));

        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertEquals("Test Transaction", result.getDescription());
        assertEquals(100, result.getAmount());
    }


    /**
     * Test pour la méthode {@link TransactionService#getUserTransactions(String)}.
     * Vérifie que la méthode lève une exception lorsque l'utilisateur est introuvable.
     */
    @Test
    void getUserTransactions_ShouldThrowEmailNotFoundException_WhenUserNotFound() {
        when(userDAO.findByEmail("user@example.com")).thenReturn(null);

        // Vérification que l'exception est levée pour un utilisateur introuvable
        assertThrows(EmailNotFoundException.class, () -> {
            transactionService.getUserTransactions("user@example.com");
        });
    }

    /**
     * Test pour la méthode {@link TransactionService#getUserTransactions(String)}.
     * Vérifie que la méthode retourne les transactions d'un utilisateur existant.
     */
    @Test
    void getUserTransactions_ShouldReturnTransactions_WhenUserExists() {
        User user = new User();
        user.setEmail("user@example.com");

        Transactions transaction1 = new Transactions();
        transaction1.setSender(user);
        transaction1.setAmount(100);

        Transactions transaction2 = new Transactions();
        transaction2.setReceiver(user);
        transaction2.setAmount(200);

        when(userDAO.findByEmail("user@example.com")).thenReturn(user);
        when(transactionDAO.findBySenderOrReceiver(user)).thenReturn(List.of(transaction1, transaction2));

        List<Transactions> result = transactionService.getUserTransactions("user@example.com");

        // Vérification des transactions retournées
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getAmount());
        assertEquals(200, result.get(1).getAmount());
    }
}

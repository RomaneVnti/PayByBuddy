package ServiceTest;

import com.paymybuddy.dao.TransactionDAO;
import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.InvalidAmountException;
import com.paymybuddy.model.Transactions;
import com.paymybuddy.model.User;
import com.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TransactionDAO transactionDAO;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService();
        // Injecter les mocks manuellement
        ReflectionTestUtils.setField(transactionService, "userDAO", userDAO);
        ReflectionTestUtils.setField(transactionService, "transactionDAO", transactionDAO);
    }

    @Test
    void addTransaction_ShouldThrowInvalidAmountException_WhenAmountIsZeroOrNegative() {
        assertThrows(InvalidAmountException.class, () -> {
            transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", 0);
        });

        assertThrows(InvalidAmountException.class, () -> {
            transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", -10);
        });
    }

    @Test
    void addTransaction_ShouldThrowEmailNotFoundException_WhenSenderNotFound() {
        when(userDAO.findByEmail("sender@example.com")).thenReturn(null);

        assertThrows(EmailNotFoundException.class, () -> {
            transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", 100);
        });
    }

    @Test
    void addTransaction_ShouldThrowEmailNotFoundException_WhenReceiverNotFound() {
        User sender = new User();
        sender.setEmail("sender@example.com");

        when(userDAO.findByEmail("sender@example.com")).thenReturn(sender);
        when(userDAO.findByEmail("receiver@example.com")).thenReturn(null);

        assertThrows(EmailNotFoundException.class, () -> {
            transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", 100);
        });
    }

    @Test
    void addTransaction_ShouldAddTransaction_WhenValidData() {
        User sender = new User();
        sender.setEmail("sender@example.com");

        User receiver = new User();
        receiver.setEmail("receiver@example.com");

        Transactions transaction = new Transactions();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription("Test Transaction");
        transaction.setAmount(100);

        // Simuler les comportements des mocks
        when(userDAO.findByEmail("sender@example.com")).thenReturn(sender);
        when(userDAO.findByEmail("receiver@example.com")).thenReturn(receiver);

        // Appeler la méthode et vérifier le comportement
        transactionService.addTransaction("sender@example.com", "receiver@example.com", "Test Transaction", 100);

        // Vérification que save a bien été appelé sans vérifier le retour
        verify(transactionDAO).save(any(Transactions.class));  // Vérifie que save a été appelé
    }


    @Test
    void getUserTransactions_ShouldThrowEmailNotFoundException_WhenUserNotFound() {
        when(userDAO.findByEmail("user@example.com")).thenReturn(null);

        assertThrows(EmailNotFoundException.class, () -> {
            transactionService.getUserTransactions("user@example.com");
        });
    }

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

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getAmount());
        assertEquals(200, result.get(1).getAmount());
    }
}

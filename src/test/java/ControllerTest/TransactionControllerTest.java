package com.paymybuddy.controller;

import com.paymybuddy.model.Transactions;
import com.paymybuddy.model.User;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.InvalidAmountException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitaire pour le contrôleur {@link TransactionController}.
 * Utilise Mockito pour simuler les dépendances et tester les comportements du contrôleur.
 */
@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Claims claims;

    private String validToken = "valid-token";
    private String currentUserEmail = "test@example.com";
    private TransactionController.TransactionRequest transactionRequest;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     */
    @BeforeEach
    void setUp() {
        transactionRequest = new TransactionController.TransactionRequest();
        transactionRequest.setReceiverEmail("receiver@example.com");
        transactionRequest.setDescription("Test transaction");
        transactionRequest.setAmount(100.0);
    }

    /**
     * Test pour la création d'une transaction valide.
     * Vérifie que la réponse HTTP est Created (201) et que la transaction créée correspond à celle attendue.
     */
    @Test
    void createTransaction_ShouldReturnCreated_WhenValidRequest() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        User senderUser = new User();
        senderUser.setEmail(currentUserEmail);

        User receiverUser = new User();
        receiverUser.setEmail(transactionRequest.getReceiverEmail());

        Transactions transaction = new Transactions();
        transaction.setSender(senderUser);
        transaction.setReceiver(receiverUser);
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setAmount(transactionRequest.getAmount());

        when(transactionService.addTransaction(anyString(), anyString(), anyString(), anyDouble()))
                .thenReturn(transaction);

        ResponseEntity<?> response = transactionController.createTransaction("Bearer " + validToken, transactionRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Transactions responseBody = (Transactions) response.getBody();
        assertNotNull(responseBody);
        assertEquals(transaction.getSender().getEmail(), responseBody.getSender().getEmail());
        assertEquals(transaction.getReceiver().getEmail(), responseBody.getReceiver().getEmail());
    }

    /**
     * Test pour la gestion d'un token invalide ou expiré lors de la création de la transaction.
     * Vérifie que la réponse HTTP est Unauthorized (401) et que le message d'erreur approprié est renvoyé.
     */
    @Test
    void createTransaction_ShouldReturnUnauthorized_WhenTokenIsInvalid() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = transactionController.createTransaction("Bearer " + validToken, transactionRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token invalide ou expiré", ((TransactionController.ApiResponse) response.getBody()).getMessage());
    }

    /**
     * Test pour la gestion de l'exception lorsque l'email du destinataire n'est pas trouvé.
     * Vérifie que la réponse HTTP est Bad Request (400) et que le message d'erreur est "Email not found".
     */
    @Test
    void createTransaction_ShouldReturnBadRequest_WhenEmailNotFound() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        when(transactionService.addTransaction(anyString(), anyString(), anyString(), anyDouble()))
                .thenThrow(new EmailNotFoundException("Email not found"));

        ResponseEntity<?> response = transactionController.createTransaction("Bearer " + validToken, transactionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email not found", ((TransactionController.ApiResponse) response.getBody()).getMessage());
    }

    /**
     * Test pour la gestion de l'exception lorsque le montant de la transaction est invalide.
     * Vérifie que la réponse HTTP est Bad Request (400) et que le message d'erreur est "Invalid amount".
     */
    @Test
    void createTransaction_ShouldReturnBadRequest_WhenInvalidAmount() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        when(transactionService.addTransaction(anyString(), anyString(), anyString(), anyDouble()))
                .thenThrow(new InvalidAmountException("Invalid amount"));

        ResponseEntity<?> response = transactionController.createTransaction("Bearer " + validToken, transactionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid amount", ((TransactionController.ApiResponse) response.getBody()).getMessage());
    }

    /**
     * Test pour la gestion d'une erreur générale lors de la création de la transaction.
     * Vérifie que la réponse HTTP est Bad Request (400) et que le message d'erreur indique un problème général.
     */
    @Test
    void createTransaction_ShouldReturnBadRequest_WhenGeneralError() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        when(transactionService.addTransaction(anyString(), anyString(), anyString(), anyDouble()))
                .thenThrow(new RuntimeException("General error"));

        ResponseEntity<?> response = transactionController.createTransaction("Bearer " + validToken, transactionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erreur lors de la création de la transaction", ((TransactionController.ApiResponse) response.getBody()).getMessage());
    }

    /**
     * Test pour récupérer les transactions d'un utilisateur.
     * Vérifie que la réponse HTTP est OK (200) et que la liste des transactions est renvoyée.
     */
    @Test
    void getUserTransactions_ShouldReturnOk_WhenTokenIsValid() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        List<Transactions> transactionList = new ArrayList<>();
        Transactions transaction = new Transactions();
        transaction.setAmount(50.0);
        transaction.setDescription("Test");
        transactionList.add(transaction);

        when(transactionService.getUserTransactions(currentUserEmail)).thenReturn(transactionList);

        ResponseEntity<?> response = transactionController.getUserTransactions("Bearer " + validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        TransactionController.ApiResponse apiResponse = (TransactionController.ApiResponse) response.getBody();
        assertNotNull(apiResponse);
        assertEquals("Transactions récupérées avec succès", apiResponse.getMessage());
        assertEquals(transactionList, apiResponse.getData());
    }

    /**
     * Test pour la gestion d'un token invalide ou expiré lors de la récupération des transactions.
     * Vérifie que la réponse HTTP est Unauthorized (401) et que le message d'erreur approprié est renvoyé.
     */
    @Test
    void getUserTransactions_ShouldReturnUnauthorized_WhenTokenInvalid() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = transactionController.getUserTransactions("Bearer " + validToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        TransactionController.ApiResponse apiResponse = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Token invalide ou expiré", apiResponse.getMessage());
    }

    /**
     * Test pour la gestion de l'exception lorsque l'email de l'utilisateur est introuvable lors de la récupération des transactions.
     * Vérifie que la réponse HTTP est Bad Request (400) et que le message d'erreur est "Email not found".
     */
    @Test
    void getUserTransactions_ShouldReturnBadRequest_WhenEmailNotFound() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        when(transactionService.getUserTransactions(currentUserEmail))
                .thenThrow(new EmailNotFoundException("Email not found"));

        ResponseEntity<?> response = transactionController.getUserTransactions("Bearer " + validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        TransactionController.ApiResponse apiResponse = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Email not found", apiResponse.getMessage());
    }

    /**
     * Test pour la gestion d'une erreur générale lors de la récupération des transactions.
     * Vérifie que la réponse HTTP est Bad Request (400) et que le message d'erreur indique un problème général.
     */
    @Test
    void getUserTransactions_ShouldReturnBadRequest_WhenGeneralErrorOccurs() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        when(transactionService.getUserTransactions(currentUserEmail))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = transactionController.getUserTransactions("Bearer " + validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        TransactionController.ApiResponse apiResponse = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Erreur lors de la récupération des transactions", apiResponse.getMessage());
    }
}

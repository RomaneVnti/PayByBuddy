package com.paymybuddy.controller;

import com.paymybuddy.model.Transactions;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour le contrôleur {@link TransactionController}.
 * Utilise JUnit 5 et Mockito pour simuler le comportement des services et vérifier le bon fonctionnement des méthodes du contrôleur.
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


    private String bearerToken = "Bearer " + validToken;


    private String currentUserEmail = "test@example.com";


    private TransactionController.TransactionRequest transactionRequest;

    /**
     * Initialisation avant chaque test.
     * Crée une demande de transaction avec des données prédéfinies.
     */
    @BeforeEach
    void setUp() {
        transactionRequest = new TransactionController.TransactionRequest();
        transactionRequest.setReceiverEmail("receiver@example.com");
        transactionRequest.setDescription("Test transaction");
        transactionRequest.setAmount(100.0);
    }

    /**
     * Teste la création d'une transaction avec des données valides.
     * Vérifie que la transaction est correctement créée et que la réponse est "Created".
     */
    @Test
    void createTransaction_ShouldReturnCreated_WhenValidRequest() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        Transactions expectedTransaction = new Transactions();
        expectedTransaction.setDescription(transactionRequest.getDescription());
        expectedTransaction.setAmount(transactionRequest.getAmount());

        when(transactionService.addTransaction(any(), any(), any(), anyDouble()))
                .thenReturn(expectedTransaction);

        ResponseEntity<?> response = transactionController.createTransaction(bearerToken, transactionRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedTransaction, response.getBody());
    }

    /**
     * Teste la création d'une transaction avec un token invalide.
     * Vérifie que la réponse retournée est "Unauthorized".
     */
    @Test
    void createTransaction_ShouldReturnUnauthorized_WhenTokenInvalid() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = transactionController.createTransaction(bearerToken, transactionRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        TransactionController.ApiResponse body = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Token invalide ou expiré", body.getMessage());
        assertNull(body.getData());
    }

    /**
     * Teste la création d'une transaction avec un email non trouvé.
     * Vérifie que la réponse retournée est "BadRequest" avec le message d'erreur.
     */
    @Test
    void createTransaction_ShouldReturnBadRequest_WhenEmailNotFound() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(transactionService.addTransaction(any(), any(), any(), anyDouble()))
                .thenThrow(new EmailNotFoundException("Email not found"));

        ResponseEntity<?> response = transactionController.createTransaction(bearerToken, transactionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        TransactionController.ApiResponse body = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Email not found", body.getMessage());
    }

    /**
     * Teste la création d'une transaction avec un montant invalide.
     * Vérifie que la réponse retournée est "BadRequest" avec le message d'erreur.
     */
    @Test
    void createTransaction_ShouldReturnBadRequest_WhenInvalidAmount() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(transactionService.addTransaction(any(), any(), any(), anyDouble()))
                .thenThrow(new InvalidAmountException("Invalid amount"));

        ResponseEntity<?> response = transactionController.createTransaction(bearerToken, transactionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        TransactionController.ApiResponse body = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Invalid amount", body.getMessage());
    }

    /**
     * Teste la création d'une transaction lorsqu'une erreur générale se produit.
     * Vérifie que la réponse retournée est "BadRequest" avec un message d'erreur générique.
     */
    @Test
    void createTransaction_ShouldReturnBadRequest_WhenGeneralError() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(transactionService.addTransaction(any(), any(), any(), anyDouble()))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = transactionController.createTransaction(bearerToken, transactionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        TransactionController.ApiResponse body = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Erreur lors de la création de la transaction", body.getMessage());
    }

    /**
     * Teste la récupération des transactions de l'utilisateur avec un token valide.
     * Vérifie que la réponse retournée est "OK" et contient les transactions de l'utilisateur.
     */
    @Test
    void getUserTransactions_ShouldReturnOk_WhenValidToken() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        List<Transactions> transactions = List.of(new Transactions());

        when(transactionService.getUserTransactions(currentUserEmail)).thenReturn(transactions);

        ResponseEntity<?> response = transactionController.getUserTransactions(bearerToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> outerMap = (Map<String, Object>) response.getBody();
        Map<String, Object> dataMap = (Map<String, Object>) outerMap.get("data");

        assertEquals(transactions, dataMap.get("transactions"));
    }

    /**
     * Teste la récupération des transactions de l'utilisateur avec un token invalide.
     * Vérifie que la réponse retournée est "Unauthorized".
     */
    @Test
    void getUserTransactions_ShouldReturnUnauthorized_WhenTokenInvalid() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = transactionController.getUserTransactions(bearerToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        TransactionController.ApiResponse body = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Token invalide ou expiré", body.getMessage());
    }

    /**
     * Teste la récupération des transactions de l'utilisateur avec un email non trouvé.
     * Vérifie que la réponse retournée est "BadRequest" avec le message d'erreur.
     */
    @Test
    void getUserTransactions_ShouldReturnBadRequest_WhenEmailNotFound() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(transactionService.getUserTransactions(currentUserEmail))
                .thenThrow(new EmailNotFoundException("Email not found"));

        ResponseEntity<?> response = transactionController.getUserTransactions(bearerToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        TransactionController.ApiResponse body = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Email not found", body.getMessage());
    }

    /**
     * Teste la récupération des transactions de l'utilisateur lorsqu'une erreur générale se produit.
     * Vérifie que la réponse retournée est "BadRequest" avec un message d'erreur générique.
     */
    @Test
    void getUserTransactions_ShouldReturnBadRequest_WhenGeneralErrorOccurs() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(transactionService.getUserTransactions(currentUserEmail))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = transactionController.getUserTransactions(bearerToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        TransactionController.ApiResponse body = (TransactionController.ApiResponse) response.getBody();
        assertEquals("Erreur lors de la récupération des transactions", body.getMessage());
    }
}

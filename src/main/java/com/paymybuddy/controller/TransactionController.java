package com.paymybuddy.controller;

import com.paymybuddy.model.Transactions;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.exception.EmailNotFoundException;
import com.paymybuddy.exception.InvalidAmountException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Contrôleur pour gérer les transactions des utilisateurs.
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Crée une transaction entre l'utilisateur connecté et un destinataire.
     */
    @PostMapping
    public ResponseEntity<?> createTransaction(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TransactionRequest transactionRequest) {

        try {
            String token = authorizationHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
            String currentUserEmail = claims.getSubject();

            Transactions transaction = transactionService.addTransaction(
                    currentUserEmail,
                    transactionRequest.getReceiverEmail(),
                    transactionRequest.getDescription(),
                    transactionRequest.getAmount()
            );

            return new ResponseEntity<>(transaction, HttpStatus.CREATED);

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Token invalide ou expiré", null));
        } catch (EmailNotFoundException | InvalidAmountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Erreur lors de la création de la transaction", null));
        }
    }

    /**
     * Récupère toutes les transactions de l'utilisateur connecté.
     */
    @GetMapping
    public ResponseEntity<?> getUserTransactions(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
            String currentUserEmail = claims.getSubject();

            var transactions = transactionService.getUserTransactions(currentUserEmail);

            return ResponseEntity.ok()
                    .body(Collections.singletonMap("data", Collections.singletonMap("transactions", transactions)));

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Token invalide ou expiré", null));
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Erreur lors de la récupération des transactions", null));
        }
    }

    /**
     * Représente une requête de création de transaction.
     */
    public static class TransactionRequest {
        private String receiverEmail;
        private String description;
        private double amount;

        public String getReceiverEmail() {
            return receiverEmail;
        }

        public void setReceiverEmail(String receiverEmail) {
            this.receiverEmail = receiverEmail;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    /**
     * Réponse standard de l'API.
     */
    public static class ApiResponse {
        private String message;
        private Object data;

        public ApiResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}

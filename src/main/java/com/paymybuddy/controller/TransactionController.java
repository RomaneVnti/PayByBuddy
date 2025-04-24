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

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Route pour créer une transaction
    @PostMapping
    public ResponseEntity<?> createTransaction(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TransactionRequest transactionRequest) {

        try {
            // Extraire le token JWT de l'en-tête Authorization
            String token = authorizationHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
            String currentUserEmail = claims.getSubject(); // Email de l'utilisateur authentifié

            // Créer la transaction en utilisant les informations envoyées dans le corps de la requête
            Transactions transaction = transactionService.addTransaction(
                    currentUserEmail,
                    transactionRequest.getReceiverEmail(),
                    transactionRequest.getDescription(),
                    transactionRequest.getAmount()
            );

            // Retourner une réponse avec le statut CREATED et la transaction
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);

        } catch (JwtException e) {
            // Si le token est invalide ou expiré, retournez une erreur 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Token invalide ou expiré", null));
        } catch (EmailNotFoundException e) {
            // Gérer l'exception EmailNotFoundException
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (InvalidAmountException e) {
            // Gérer l'exception InvalidAmountException
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            // Si une autre erreur survient, retournez une erreur 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erreur lors de la création de la transaction", null));
        }
    }

    // Classe interne pour la structure de la requête de transaction
    public static class TransactionRequest {
        private String receiverEmail;
        private String description;
        private double amount;

        // Getters et setters
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

    // Structure de la réponse
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

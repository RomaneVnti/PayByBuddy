package com.paymybuddy.controller;

import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.controller.HomeController.ApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Claims claims;

    private String validToken = "valid-token";

    @BeforeEach
    void setUp() {
        // On simule la récupération du nom d'utilisateur dans le token de manière leniente
        lenient().when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        lenient().when(claims.getSubject()).thenReturn("test@example.com");
    }

    @Test
    void showInscriptionPage_ShouldReturnInscriptionForm() {
        ResponseEntity<?> response = homeController.showInscriptionPage();
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertNotNull(apiResponse);
        assertEquals("Inscription", apiResponse.getMessage());
        assertTrue(apiResponse.getData().contains("Formulaire d'inscription"));
    }

    @Test
    void showConnexionPage_ShouldReturnConnexionForm() {
        ResponseEntity<?> response = homeController.showConnexionPage();
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertNotNull(apiResponse);
        assertEquals("Connection", apiResponse.getMessage());
        assertTrue(apiResponse.getData().contains("Formulaire de connection"));
    }

    @Test
    void showHomePage_ShouldReturnTransactionInfo_WhenValidToken() {
        String authorizationHeader = "Bearer " + validToken;

        ResponseEntity<?> response = homeController.showHomePage(authorizationHeader);
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertNotNull(apiResponse);
        assertEquals("Success", apiResponse.getMessage());
        assertTrue(apiResponse.getData().contains("Transactions de test@example.com"));
    }

    @Test
    void showRelationsPage_ShouldReturnRelationsInfo_WhenValidToken() {
        String authorizationHeader = "Bearer " + validToken;

        ResponseEntity<?> response = homeController.showRelationsPage(authorizationHeader);
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertNotNull(apiResponse);
        assertEquals("Success", apiResponse.getMessage());
        assertTrue(apiResponse.getData().contains("Relations de test@example.com"));
    }

    @Test
    void showTransfertPage_ShouldReturnTransfertInfo_WhenValidToken() {
        String authorizationHeader = "Bearer " + validToken;

        ResponseEntity<?> response = homeController.showTransfertPage(authorizationHeader);
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertNotNull(apiResponse);
        assertEquals("Success", apiResponse.getMessage());
        assertTrue(apiResponse.getData().contains("Transferts de test@example.com"));
    }

    @Test
    void showProfilPage_ShouldReturnProfilInfo_WhenValidToken() {
        String authorizationHeader = "Bearer " + validToken;

        ResponseEntity<?> response = homeController.showProfilPage(authorizationHeader);
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertNotNull(apiResponse);
        assertEquals("Success", apiResponse.getMessage());
        assertTrue(apiResponse.getData().contains("Profil de test@example.com"));
    }

    @Test
    void showHomePage_ShouldReturnUnauthorized_WhenInvalidToken() {
        String invalidToken = "invalid-token";
        String authorizationHeader = "Bearer " + invalidToken;

        when(jwtTokenProvider.getClaimsFromToken(invalidToken)).thenThrow(JwtException.class);

        ResponseEntity<?> response = homeController.showHomePage(authorizationHeader);
        ApiResponse apiResponse = (ApiResponse) response.getBody();

        assertNotNull(apiResponse);
        assertEquals("Token invalide ou expiré", apiResponse.getMessage());
        assertNull(apiResponse.getData());
    }

}

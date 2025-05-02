package com.paymybuddy.controller;

import com.paymybuddy.service.RelationService;
import com.paymybuddy.security.JwtTokenProvider;
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
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour le contrôleur {@link RelationController}.
 * Teste la logique de gestion des relations utilisateur, y compris l'ajout et la récupération,
 * en simulant les dépendances via Mockito.
 */
@ExtendWith(MockitoExtension.class)
class RelationControllerTest {

    @InjectMocks
    private RelationController relationController;

    @Mock
    private RelationService relationService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Claims claims;

    @Mock
    private Model model;

    private String validToken = "valid-token";
    private String bearerToken = "Bearer " + validToken;
    private String currentUserEmail = "test@example.com";
    private RelationController.AddRelationRequest addRelationRequest;

    /**
     * Initialise les objets nécessaires avant chaque test.
     */
    @BeforeEach
    void setUp() {
        addRelationRequest = new RelationController.AddRelationRequest();
        addRelationRequest.setRelationEmail("friend@example.com");
    }

    /**
     * Vérifie que l'ajout d'une relation retourne une vue "relations"
     * et affiche un message de succès lorsque l'ajout réussit.
     */
    @Test
    void addRelation_ShouldReturnSuccess_WhenRelationAdded() {
        when(jwtTokenProvider.getClaimsFromToken(bearerToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(relationService.addRelation(currentUserEmail, addRelationRequest.getRelationEmail())).thenReturn(true);

        String result = relationController.addRelation(bearerToken, addRelationRequest.getRelationEmail(), model);

        assertEquals("relations", result);
        verify(model).addAttribute("message", "Relation ajoutée avec succès !");
    }

    /**
     * Vérifie que l'ajout d'une relation retourne un message d’échec
     * lorsque la relation existe déjà.
     */
    @Test
    void addRelation_ShouldReturnFailure_WhenRelationExists() {
        when(jwtTokenProvider.getClaimsFromToken(bearerToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(relationService.addRelation(currentUserEmail, addRelationRequest.getRelationEmail())).thenReturn(false);

        String result = relationController.addRelation(bearerToken, addRelationRequest.getRelationEmail(), model);

        assertEquals("relations", result);
        verify(model).addAttribute("message", "Échec de l'ajout de la relation (elle existe peut-être déjà).");
    }

    /**
     * Vérifie que l'utilisateur est redirigé vers la page de connexion
     * si le token JWT est invalide.
     */
    @Test
    void addRelation_ShouldRedirectToLogin_WhenTokenIsInvalid() {
        when(jwtTokenProvider.getClaimsFromToken(bearerToken)).thenThrow(new JwtException("Invalid token"));

        String result = relationController.addRelation(bearerToken, addRelationRequest.getRelationEmail(), model);

        assertEquals("redirect:/connexion", result);
    }

    /**
     * Vérifie que le contrôleur affiche une erreur appropriée
     * si une exception inattendue est levée pendant l’ajout de la relation.
     */
    @Test
    void addRelation_ShouldReturnError_WhenExceptionOccurs() {
        when(jwtTokenProvider.getClaimsFromToken(bearerToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);
        when(relationService.addRelation(currentUserEmail, addRelationRequest.getRelationEmail()))
                .thenThrow(new RuntimeException("Unexpected error"));

        String result = relationController.addRelation(bearerToken, addRelationRequest.getRelationEmail(), model);

        assertEquals("relations", result);
        verify(model).addAttribute("message", "Une erreur est survenue : Unexpected error");
    }

    /**
     * Vérifie que la récupération des relations fonctionne correctement
     * lorsque le token est valide et que des relations existent.
     */
    @Test
    void getUserRelations_ShouldReturnOk_WhenTokenIsValidAndRelationsExist() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        List<String> relations = Arrays.asList("friend@example.com", "anotherfriend@example.com");
        when(relationService.getUserRelations(currentUserEmail)).thenReturn(relations);

        ResponseEntity<?> response = relationController.getUserRelations(validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        RelationController.ApiResponse apiResponse = (RelationController.ApiResponse) response.getBody();
        assertEquals("Relations récupérées avec succès", apiResponse.getMessage());
        assertEquals(relations, apiResponse.getData());
    }

    /**
     * Vérifie que la récupération des relations retourne un succès même
     * lorsque l'utilisateur n'a aucune relation enregistrée.
     */
    @Test
    void getUserRelations_ShouldReturnOk_WhenTokenIsValidAndNoRelationsExist() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        List<String> relations = Arrays.asList(); // liste vide
        when(relationService.getUserRelations(currentUserEmail)).thenReturn(relations);

        ResponseEntity<?> response = relationController.getUserRelations(validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        RelationController.ApiResponse apiResponse = (RelationController.ApiResponse) response.getBody();
        assertEquals("Relations récupérées avec succès", apiResponse.getMessage());
        assertEquals(relations, apiResponse.getData());
    }

    /**
     * Vérifie que le contrôleur retourne un code 401 UNAUTHORIZED
     * lorsque aucun token n’est fourni.
     */
    @Test
    void getUserRelations_ShouldReturnUnauthorized_WhenTokenIsEmpty() {
        ResponseEntity<?> response = relationController.getUserRelations("");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        RelationController.ApiResponse apiResponse = (RelationController.ApiResponse) response.getBody();
        assertEquals("Aucun token trouvé", apiResponse.getMessage());
    }

    /**
     * Vérifie que le contrôleur retourne un code 401 UNAUTHORIZED
     * si le token est expiré.
     */
    @Test
    void getUserRelations_ShouldReturnUnauthorized_WhenTokenIsExpired() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Token expired"));

        ResponseEntity<?> response = relationController.getUserRelations(validToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        RelationController.ApiResponse apiResponse = (RelationController.ApiResponse) response.getBody();
        assertEquals("Token invalide ou expiré", apiResponse.getMessage());
    }

    /**
     * Vérifie que le contrôleur retourne un code 401 UNAUTHORIZED
     * si le token est invalide.
     */
    @Test
    void getUserRelations_ShouldReturnUnauthorized_WhenTokenIsInvalid() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = relationController.getUserRelations(validToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        RelationController.ApiResponse apiResponse = (RelationController.ApiResponse) response.getBody();
        assertEquals("Token invalide ou expiré", apiResponse.getMessage());
    }
}

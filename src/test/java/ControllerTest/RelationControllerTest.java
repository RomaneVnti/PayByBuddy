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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitaire pour le contrôleur {@link RelationController}.
 * Utilise Mockito pour simuler les dépendances et tester les comportements du contrôleur.
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

    private String validToken = "valid-token";
    private String currentUserEmail = "test@example.com";
    private RelationController.AddRelationRequest addRelationRequest;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     */
    @BeforeEach
    void setUp() {
        addRelationRequest = new RelationController.AddRelationRequest();
        addRelationRequest.setRelationEmail("friend@example.com");
    }

    /**
     * Test pour récupérer les relations d'un utilisateur.
     * Vérifie que la réponse HTTP est OK (200) et que la liste des relations est renvoyée lorsque des relations existent.
     */
    @Test
    void getUserRelations_ShouldReturnOk_WhenRelationsExist() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        List<String> relations = Arrays.asList("friend@example.com", "anotherfriend@example.com");

        when(relationService.getUserRelations(currentUserEmail)).thenReturn(relations);

        ResponseEntity<?> response = relationController.getUserRelations("Bearer " + validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        RelationController.ApiResponse apiResponse = (RelationController.ApiResponse) response.getBody();
        assertEquals("Relations récupérées avec succès", apiResponse.getMessage());
        assertEquals(relations, apiResponse.getData());
    }

    /**
     * Test pour récupérer les relations d'un utilisateur sans relations existantes.
     * Vérifie que la réponse HTTP est OK (200) et que le message indique qu'aucune relation n'est trouvée.
     */
    @Test
    void getUserRelations_ShouldReturnOk_WhenNoRelationsFound() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        List<String> relations = Arrays.asList();

        when(relationService.getUserRelations(currentUserEmail)).thenReturn(relations);

        ResponseEntity<?> response = relationController.getUserRelations("Bearer " + validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        RelationController.ApiResponse apiResponse = (RelationController.ApiResponse) response.getBody();
        assertEquals("Aucune relation trouvée", apiResponse.getMessage());
        assertNull(apiResponse.getData());
    }

    /**
     * Test pour la gestion d'un token invalide ou expiré lors de la récupération des relations.
     * Vérifie que la réponse HTTP est Unauthorized (401) et que le message d'erreur approprié est renvoyé.
     */
    @Test
    void getUserRelations_ShouldReturnUnauthorized_WhenTokenIsInvalid() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = relationController.getUserRelations("Bearer " + validToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token invalide ou expiré", ((RelationController.ApiResponse) response.getBody()).getMessage());
    }

    /**
     * Test pour ajouter une relation avec succès.
     * Vérifie que la réponse HTTP est OK (200) et que le message indique que la relation a été ajoutée avec succès.
     */
    @Test
    void addRelation_ShouldReturnOk_WhenRelationIsAdded() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        when(relationService.addRelation(currentUserEmail, addRelationRequest.getRelationEmail())).thenReturn(true);

        ResponseEntity<?> response = relationController.addRelation("Bearer " + validToken, addRelationRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Relation added successfully", ((RelationController.ApiResponse) response.getBody()).getMessage());
    }

    /**
     * Test pour l'échec d'ajout de relation.
     * Vérifie que la réponse HTTP est Bad Request (400) et que le message d'erreur est retourné lorsque l'ajout de la relation échoue.
     */
    @Test
    void addRelation_ShouldReturnBadRequest_WhenFailedToAddRelation() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(currentUserEmail);

        when(relationService.addRelation(currentUserEmail, addRelationRequest.getRelationEmail())).thenReturn(false);

        ResponseEntity<?> response = relationController.addRelation("Bearer " + validToken, addRelationRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to add relation", ((RelationController.ApiResponse) response.getBody()).getMessage());
    }

    /**
     * Test pour la gestion d'un token invalide ou expiré lors de l'ajout d'une relation.
     * Vérifie que la réponse HTTP est Unauthorized (401) et que le message d'erreur approprié est renvoyé.
     */
    @Test
    void addRelation_ShouldReturnUnauthorized_WhenTokenIsInvalid() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));

        ResponseEntity<?> response = relationController.addRelation("Bearer " + validToken, addRelationRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token invalide ou expiré", ((RelationController.ApiResponse) response.getBody()).getMessage());
    }
}

package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour {@link HomeController}.
 */
@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private Claims claims;

    @Mock
    private Model model;

    private final String validToken = "valid-token";

    @BeforeEach
    void setUp() {
        lenient().when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        lenient().when(claims.getSubject()).thenReturn("test@example.com");
    }

    /**
     * Vérifie que la page d'inscription retourne la vue "inscription".
     */
    @Test
    void showInscriptionPage_ReturnsInscriptionView() {
        String result = homeController.showInscriptionPage();
        assertEquals("inscription", result);
    }

    /**
     * Vérifie que la page de connexion retourne la vue "connexion".
     */
    @Test
    void showConnexionPage_ReturnsConnexionView() {
        String result = homeController.showConnexionPage();
        assertEquals("connexion", result);
    }

    /**
     * Vérifie que la page d'accueil retourne "home" avec un token valide.
     */
    @Test
    void showHomePage_WithValidToken_ReturnsHomeView() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        String result = homeController.showHomePage(validToken);
        assertEquals("home", result);
    }

    /**
     * Vérifie que la page d'accueil redirige vers "connexion" si le token est invalide.
     */
    @Test
    void showHomePage_WithInvalidToken_ReturnsRedirectToConnexion() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));
        String result = homeController.showHomePage(validToken);
        assertEquals("redirect:/connexion", result);
    }

    /**
     * Vérifie que la page des relations retourne "relations" avec un token valide.
     */
    @Test
    void showRelationPage_WithValidToken_ReturnsRelationView() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        String result = homeController.showRelationPage(validToken);
        assertEquals("relations", result);
    }

    /**
     * Vérifie que la page des relations redirige vers "connexion" si le token est invalide.
     */
    @Test
    void showRelationPage_WithInvalidToken_ReturnsRedirectToConnexion() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));
        String result = homeController.showRelationPage(validToken);
        assertEquals("redirect:/connexion", result);
    }

    /**
     * Vérifie que la page de transfert retourne "transfer" avec un token valide.
     */
    @Test
    void showTransferPage_WithValidToken_ReturnsTransferView() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        String result = homeController.showTransferPage(validToken);
        assertEquals("transfer", result);
    }

    /**
     * Vérifie que la page de transfert redirige vers "connexion" si le token est invalide.
     */
    @Test
    void showTransferPage_WithInvalidToken_ReturnsRedirectToConnexion() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));
        String result = homeController.showTransferPage(validToken);
        assertEquals("redirect:/connexion", result);
    }

    /**
     * Vérifie que la page de profil retourne "profil" si l'utilisateur est trouvé et le token valide.
     */
    @Test
    void showProfilPage_WithValidToken_ReturnsProfilView() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenReturn(claims);
        when(userService.findUserByEmail("test@example.com")).thenReturn(new User());
        String result = homeController.showProfilPage(validToken, model);
        assertEquals("profil", result);
    }

    /**
     * Vérifie que la page de profil redirige vers "connexion" si le token est invalide.
     */
    @Test
    void showProfilPage_WithInvalidToken_ReturnsRedirectToConnexion() {
        when(jwtTokenProvider.getClaimsFromToken(validToken)).thenThrow(new JwtException("Invalid token"));
        String result = homeController.showProfilPage(validToken, model);
        assertEquals("redirect:/connexion", result);
    }

    /**
     * Vérifie que la page de profil redirige vers "connexion" si aucun token n'est fourni.
     */
    @Test
    void showProfilPage_WithoutToken_ReturnsRedirectToConnexion() {
        String result = homeController.showProfilPage("", model);
        assertEquals("redirect:/connexion", result);
    }
}

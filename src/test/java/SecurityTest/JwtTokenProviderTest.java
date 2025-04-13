package SecurityTest;

import com.paymybuddy.security.JwtTokenProvider;
import com.paymybuddy.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @Mock
    private User user;  // Mock de l'utilisateur

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;  // Injection de la dépendance dans JwtTokenProvider

    private String token;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        when(user.getEmail()).thenReturn("testuser@example.com");  // Mock de l'email de l'utilisateur

        token = jwtTokenProvider.generateToken(user);  // Génération d'un token pour les tests
    }

    /**
     * Test pour la génération d'un token JWT.
     * Vérifie que le token généré n'est pas nul et qu'il a une structure valide.
     */
    @Test
    void testGenerateToken() {
        // Vérification que le token généré n'est pas nul
        assertNotNull(token);
        assertTrue(token.length() > 0);  // Vérification que le token a une longueur significative
    }

    /**
     * Test pour l'extraction des claims d'un token JWT.
     * Vérifie que les claims contiennent les bonnes informations.
     */
    @Test
    void testGetClaimsFromToken() {
        // Extraction des claims du token
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);

        // Vérification que les claims ne sont pas nuls et que le sujet est correct
        assertNotNull(claims);
        assertEquals("testuser@example.com", claims.getSubject());  // Vérification du sujet (email de l'utilisateur)
    }

    /**
     * Test pour l'extraction du nom d'utilisateur (sujet) d'un token JWT.
     * Vérifie que l'extraction du nom d'utilisateur est correcte.
     */
    @Test
    void testGetUsernameFromToken() {
        // Extraction du nom d'utilisateur à partir du token
        String username = jwtTokenProvider.getUsernameFromToken(token);

        // Vérification que le nom d'utilisateur extrait est correct
        assertEquals("testuser@example.com", username);
    }

    /**
     * Test pour la validation d'un token valide.
     * Vérifie que la validation renvoie true pour un token valide.
     */
    @Test
    void testValidateToken_Valid() {
        // Validation du token (il doit être valide)
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Vérification que le token est valide
        assertTrue(isValid);
    }


    /**
     * Test pour la gestion des erreurs lors de la récupération des claims d'un token invalide.
     * Vérifie que l'exception est levée si le token est invalide.
     */
    @Test
    void testGetClaimsFromToken_InvalidToken() {
        String invalidToken = "invalidToken";

        // Test de l'exception levée lors de l'extraction des claims d'un token invalide
        assertThrows(JwtException.class, () -> {
            jwtTokenProvider.getClaimsFromToken(invalidToken);
        });
    }

    // Helper method to mock an expired token
    private String mockExpiredToken() {
        long expiredValidity = -3600000; // Expiration dans le passé (1 heure avant)
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiredValidity);

        return Jwts.builder()
                .setSubject("testuser@example.com")
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(jwtTokenProvider.getSigningKey())
                .compact();
    }
}

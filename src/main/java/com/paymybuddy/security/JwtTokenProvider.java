package com.paymybuddy.security;

import com.paymybuddy.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Fournisseur de token JWT.
 * Cette classe est responsable de la création, de la validation
 * et de l'extraction des informations d'un token JWT.
 */
@Component
public class JwtTokenProvider {

    /**
     * Clé secrète utilisée pour signer les tokens JWT.
     */
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Durée de validité d'un token (1 heure).
     */
    private final long validityInMilliseconds = 3600000;

    /**
     * Génère un token JWT basé sur l'utilisateur connecté.
     *
     * @param user l'utilisateur pour lequel générer le token
     * @return une chaîne représentant le token JWT
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Extrait les informations (claims) du token JWT.
     *
     * @param token le token JWT à parser
     * @return les claims extraits du token
     */
    public Claims getClaimsFromToken(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(secretKey)
                .build();
        return parser.parseClaimsJws(token).getBody();
    }

    /**
     * Extrait l'identifiant de l'utilisateur (email) à partir du token.
     *
     * @param token le token JWT
     * @return le sujet (email de l'utilisateur)
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Vérifie si le token est encore valide (non expiré).
     *
     * @param token le token JWT à valider
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Vérifie si le token est expiré.
     *
     * @param token le token JWT à vérifier
     * @return true si le token est expiré, false sinon
     */
    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    /**
     * Retourne la clé de signature utilisée pour les JWT.
     *
     * @return la clé secrète
     */
    public Key getSigningKey() {
        return secretKey;
    }
}

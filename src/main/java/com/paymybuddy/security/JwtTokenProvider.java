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

@Component
public class JwtTokenProvider {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // Clé générée pour HS256
    // Utilisez une clé secrète appropriée
    private final long validityInMilliseconds = 3600000; // 1 heure

    // Générer un JWT à partir de l'utilisateur
    public String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(user.getEmail())  // Utiliser l'email de l'utilisateur comme sujet
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Extraire les informations (claims) du token
    public Claims getClaimsFromToken(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(secretKey)
                .build();
        return parser.parseClaimsJws(token).getBody();
    }

    // Extraire le nom d'utilisateur (sujet) du token
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Vérifier si un token est valide
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // Vérifier si le token a expiré
    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }
}

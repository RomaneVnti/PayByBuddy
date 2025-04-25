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
















}

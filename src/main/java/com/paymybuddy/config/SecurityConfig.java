package com.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/user/create")  // Désactiver CSRF pour cette route uniquement
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/user/create").permitAll()  // Permet l'accès à la route de création d'utilisateur sans authentification
                        .anyRequest().authenticated()  // Authentifie toutes les autres requêtes
                )
                .formLogin(withDefaults()); // Utiliser le formulaire de connexion de Spring Security (si tu en as un)

        return http.build();
    }
}
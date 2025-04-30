package com.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/user/create")
                        .ignoringRequestMatchers("auth/login")
                                .ignoringRequestMatchers("/home")
                                .ignoringRequestMatchers("/relations")
                                .ignoringRequestMatchers("/transfert")
                                .ignoringRequestMatchers("/profil")
                                .ignoringRequestMatchers("/inscription")
                                .ignoringRequestMatchers("/connexion")
                        .ignoringRequestMatchers("/user-relations")
                        .ignoringRequestMatchers("/user/relation/add")
                        .ignoringRequestMatchers("/transaction")
                        .ignoringRequestMatchers("/user/update")
                        .ignoringRequestMatchers("/css/**")  // Autoriser l'accès au répertoire CSS
                        .ignoringRequestMatchers("/js/**")
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/user/create").permitAll()
                        .requestMatchers("auth/login").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/relations").permitAll()
                        .requestMatchers("/transfert").permitAll()
                        .requestMatchers("/profil").permitAll()
                        .requestMatchers("/inscription").permitAll()
                        .requestMatchers("/connexion").permitAll()
                        .requestMatchers("/user-relations").permitAll()
                        .requestMatchers("/user/relation/add").permitAll()
                        .requestMatchers("/transaction").permitAll()
                        .requestMatchers("/user/update").permitAll()
                        .requestMatchers("/css/**").permitAll()  // Permet l'accès aux fichiers CSS
                        .requestMatchers("/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin().disable();

        return http.build();
    }
}
package com.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.Customizer.withDefaults;

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
                        .ignoringRequestMatchers("/api/user/create")
                        .ignoringRequestMatchers("/api/auth/login")
                                .ignoringRequestMatchers("/home")
                                .ignoringRequestMatchers("/relations")
                                .ignoringRequestMatchers("/transfert")
                                .ignoringRequestMatchers("/profil")
                                .ignoringRequestMatchers("/inscription")
                                .ignoringRequestMatchers("/connection")
                        .ignoringRequestMatchers("/user/relations")
                        .ignoringRequestMatchers("/user/relation/add")
                        .ignoringRequestMatchers("/transaction")
                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/user/create").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/relations").permitAll()
                        .requestMatchers("/transfert").permitAll()
                        .requestMatchers("/profil").permitAll()
                        .requestMatchers("/inscription").permitAll()
                        .requestMatchers("/connection").permitAll()
                        .requestMatchers("/user/relations").permitAll()
                        .requestMatchers("/user/relation/add").permitAll()
                        .requestMatchers("/transaction").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin().disable();

        return http.build();
    }
}
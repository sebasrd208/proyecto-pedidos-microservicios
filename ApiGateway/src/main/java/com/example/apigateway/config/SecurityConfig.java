package com.example.apigateway.config;

import org.springframework.http.*;
import org.springframework.security.web.*;
import org.springframework.security.config.*;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.method.configuration.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.cors(Customizer.withDefaults());
        http.csrf(csrf -> {
            csrf.disable();
        });
        http.authorizeHttpRequests(auth -> {
           auth.requestMatchers("/usuarios/**").permitAll();
           auth.requestMatchers(HttpMethod.GET, "/proveedores/**", "/productos/**", "/pedidos/**").hasAnyRole("USER", "ADMIN");
           auth.requestMatchers("/proveedores/**", "/productos/**", "/pedidos/**").hasRole("ADMIN");
           auth.anyRequest().authenticated();
        });
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

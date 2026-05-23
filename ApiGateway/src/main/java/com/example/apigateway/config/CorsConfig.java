package com.example.apigateway.config;

import org.springframework.web.cors.*;
import org.springframework.web.filter.*;
import org.springframework.context.annotation.*;

@Configuration
public class CorsConfig {

    @Bean
    CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}

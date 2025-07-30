/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.billing.security;

import java.time.Duration;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author Hp
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    // Url patterns to exclude required authorization
    private static final String[] NO_AUTH_LIST = {
        "/v3/api-docs/**",//
        "/swagger-ui/**",     
        "/configuration/ui", //
        "/swagger-resources/**", //
        "/configuration/security", //   
        "/webjars/**", //
        "/login",
        "/h2-console/**"};
         
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Version 1. Full security in order to ask by user and password before to acces swagger ui
        http
            .authorizeHttpRequests((authorize) -> authorize
                .anyRequest().authenticated()
            ) 
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());
               
        // Version 2. Custom security configuration, we can excluse some paths and ask by user and password before each request to acces swagger ui
        /*http
            // Settings cors here is useless right now cause Swagger runs on the same site, but we're practising
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers(NO_AUTH_LIST).permitAll()
                .requestMatchers(HttpMethod.POST, "/billing/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/billing/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());*/
        return http.build();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
       
        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Origin", "Accept", "X-Requested-With", "Content-Type", 
            "Access-Control-Request-Method", "Access-Control-Request-Headers", 
            "Authorization", "Cache-Control"
        ));        
        
        // Exposed headers
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials",
            "Access-Control-Allow-Headers", "Access-Control-Max-Age"
        ));
        
        
        /*configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",  // Angular
        ));*/
        
        // All origins allowed (ONLY FOR DEVELOPMENT)
        configuration.addAllowedOriginPattern("*");
        
        // Allowed methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // Cache requests for 1 hour
        configuration.setMaxAge(Duration.ofHours(1));

        // Allow credentials
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}

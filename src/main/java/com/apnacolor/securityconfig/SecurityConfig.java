package com.apnacolor.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//
@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/products/add").permitAll() 
                .requestMatchers("/api/products/all").permitAll() 
                .requestMatchers("/api/products/**").permitAll() 
                .requestMatchers("/apnacolor/").hasRole("ADMIN")
//                .requestMatchers("/api/login", "/api/signup", "/api/products", "/images/**" ,"/auth/**").permitAll()
                .requestMatchers("/auth/login", "/auth/signup", "/api/products", "/images/**" ,"/auth/**").permitAll()
                .requestMatchers("/api/cart/**").permitAll()
                .requestMatchers("/Billing/**").permitAll()
                .requestMatchers("/Billing/api/bill").permitAll() 
                .requestMatchers("/api/feedback/**").permitAll()
//                .requestMatchers("/api/cart/**").permitAll()  // <--- This line
                .anyRequest().authenticated()
            )
            .formLogin().disable()
            .httpBasic().disable();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}




/*
package com.example.burningkey.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

   */
/* @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/***")
                        .ignoringRequestMatchers("/api/v1/users/***")
                        .ignoringRequestMatchers("/api/v1/texts")
                        .ignoringRequestMatchers("/api/v1/texts/***")
                        .ignoringRequestMatchers("/multiplayer/**")
                )
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/api/v1/texts/***").permitAll()
                        .requestMatchers("/api/v1/users/***").permitAll()
                        .requestMatchers("/api/v1/texts").permitAll()
                        .requestMatchers("/h2-console/***").permitAll()
                        .requestMatchers("/multiplayer/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().sameOrigin())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }*//*

}
*/

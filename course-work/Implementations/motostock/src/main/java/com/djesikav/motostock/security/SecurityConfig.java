package com.djesikav.motostock.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/parts/new",
                                "/parts/*/edit",
                                "/categroies/new",
                                "/categories/*/edit",
                                "/suppliers/new",
                                "/suppliers/*/edit"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/parts/**",
                                "/categories/**",
                                "/suppliers/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/parts/**",
                                "/categories/**",
                                "/suppliers/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/parts/**",
                                "/categories/**",
                                "/suppliers/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/parts/search",
                                "/categories/search",
                                "/suppliers/search"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/parts",
                                "/parts/{id}",
                                "/categories",
                                "/categories/{id}",
                                "/suppliers",
                                "/suppliers/{id}"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }
}

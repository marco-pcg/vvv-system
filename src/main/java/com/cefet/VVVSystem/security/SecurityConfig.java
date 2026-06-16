package com.cefet.VVVSystem.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    private final org.springframework.core.env.Environment env;

    public SecurityConfig(SecurityFilter securityFilter, org.springframework.core.env.Environment env) {
        this.securityFilter = securityFilter;
        this.env = env;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF (padrão para APIs REST com JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // API
                                                                                                               // sem
                                                                                                               // estado
                                                                                                               // (Stateless)

        boolean ignorePermission = "true".equalsIgnoreCase(env.getProperty("IGNORE_PERMISSION")) ||
                "true".equalsIgnoreCase(env.getProperty("env.IGNORE_PERMISSION"));

        if (!ignorePermission) {
            try {
                java.nio.file.Path envPath = java.nio.file.Paths.get(".env");
                if (java.nio.file.Files.exists(envPath)) {
                    java.util.List<String> lines = java.nio.file.Files.readAllLines(envPath);
                    for (String line : lines) {
                        if (line.trim().startsWith("IGNORE_PERMISSION=true")
                                || line.trim().startsWith("IGNORE_PERMISSION = true")) {
                            ignorePermission = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // Ignore
            }
        }

        if (ignorePermission) {
            http.authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll());
        } else {
            http.authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Endpoint de login liberado
                                                                                 // publicamente
                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Endpoint de cadastro liberado
                                                                                    // publicamente
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Documentação
                                                                                                          // do Swagger
                                                                                                          // liberada
                    .anyRequest().authenticated() // Qualquer outra rota exige autenticação
            )
                    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); // Injeta nosso filtro
                                                                                                  // JWT antes do filtro
                                                                                                  // padrão do Spring
        }

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //
    }
}
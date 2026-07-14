package com.cefet.VVVSystem.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

@Service
public class TokenService {

    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public TokenService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        // Transforma a string secreta em uma chave criptográfica segura de 256 bits
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(String email) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(email)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(key)
                .compact();
    }

    public String validarToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject(); // Retorna o e-mail se o token for autêntico
        } catch (JwtException e) {
            return null; // Retorna null se estiver expirado, adulterado ou inválido
        }
    }
}
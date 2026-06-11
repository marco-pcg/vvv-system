package com.cefet.VVVSystem.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.security")
public class JwtProperties {
    
    private String secret = "sua-chave-secreta-padrao-caso-nao-haja-env";
    private Long expiration = 7200000L; // 2 horas em milissegundos

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public Long getExpiration() { return expiration; }
    public void setExpiration(Long expiration) { this.expiration = expiration; }
}
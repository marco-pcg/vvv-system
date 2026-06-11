package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.LoginRequestDTO;
import com.cefet.VVVSystem.dto.LoginResponseDTO;
import com.cefet.VVVSystem.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        // Encapsula as credenciais recebidas no formato do Spring Security
        var tokenDeAutenticacao = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        
        // O AuthenticationManager chama internamente seu AuthService para buscar no banco e o BCrypt para checar a senha
        Authentication auth = authenticationManager.authenticate(tokenDeAutenticacao);
        
        // Se passar pela linha acima sem lançar erro, o usuário está autenticado. Geramos o token:
        String token = tokenService.gerarToken(auth.getName());
        
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.domain.entity.Role;
import com.cefet.VVVSystem.domain.entity.User;
import com.cefet.VVVSystem.domain.repository.RoleRepository;
import com.cefet.VVVSystem.domain.repository.UserRepository;
import com.cefet.VVVSystem.dto.LoginRequestDTO;
import com.cefet.VVVSystem.dto.LoginResponseDTO;
import com.cefet.VVVSystem.dto.RegisterRequestDTO;
import com.cefet.VVVSystem.response.ApiResponse;
import com.cefet.VVVSystem.security.RoleConstants;
import com.cefet.VVVSystem.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
                          UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO data) {
        // Encapsula as credenciais recebidas no formato do Spring Security
        var tokenDeAutenticacao = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        
        // O AuthenticationManager chama internamente seu AuthService para buscar no banco e o BCrypt para checar a senha
        Authentication auth = authenticationManager.authenticate(tokenDeAutenticacao);
        
        // Se passar pela linha acima sem lançar erro, o usuário está autenticado. Geramos o token:
        String token = tokenService.gerarToken(auth.getName());
        
        return ApiResponse.success("Login realizado com sucesso", new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterRequestDTO data) {
        if (userRepository.findByUsername(data.username()).isPresent()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Nome de usuário já está em uso", "Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(data.username());
        newUser.setPassword(passwordEncoder.encode(data.password()));

        Role clienteRole = roleRepository.findByName(RoleConstants.ROLE_CLIENTE)
                .orElseThrow(() -> new RuntimeException("Role de cliente não encontrada no banco de dados."));
        
        newUser.getRoles().add(clienteRole);

        userRepository.save(newUser);

        return ApiResponse.created("Usuário registrado com sucesso", null);
    }
}
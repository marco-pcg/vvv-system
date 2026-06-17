package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.domain.entity.Cliente;
import com.cefet.VVVSystem.domain.entity.Pessoa;
import com.cefet.VVVSystem.domain.entity.Role;
import com.cefet.VVVSystem.domain.entity.User;
import com.cefet.VVVSystem.domain.repository.ClienteRepository;
import com.cefet.VVVSystem.domain.repository.PessoaRepository;
import com.cefet.VVVSystem.domain.repository.RoleRepository;
import com.cefet.VVVSystem.domain.repository.UserRepository;
import com.cefet.VVVSystem.dto.LoginRequestDTO;
import com.cefet.VVVSystem.dto.LoginResponseDTO;
import com.cefet.VVVSystem.dto.RegisterRequestDTO;
import com.cefet.VVVSystem.dto.ForgotPasswordRequestDTO;
import com.cefet.VVVSystem.dto.ResetPasswordRequestDTO;
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
    private final ClienteRepository clienteRepository;
    private final PessoaRepository pessoaRepository;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
                          UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, ClienteRepository clienteRepository,
                          PessoaRepository pessoaRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO data) {
        var tokenDeAutenticacao = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        Authentication auth = authenticationManager.authenticate(tokenDeAutenticacao);
        String token = tokenService.gerarToken(auth.getName());
        return ApiResponse.success("Login realizado com sucesso", new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterRequestDTO data) {
        if (userRepository.findByUsername(data.username()).isPresent()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Nome de usuário já está em uso", "Username already exists");
        }
        if (pessoaRepository.findByCpf(data.cpf()).isPresent()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "CPF já está em uso", "CPF already exists");
        }
        if (pessoaRepository.findByEmail(data.email()).isPresent()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "E-mail já está em uso", "Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(data.username());
        newUser.setPassword(passwordEncoder.encode(data.password()));

        Role clienteRole = roleRepository.findByName(RoleConstants.ROLE_CLIENTE)
                .orElseThrow(() -> new RuntimeException("Role de cliente não encontrada no banco de dados."));
        newUser.getRoles().add(clienteRole);
        newUser = userRepository.save(newUser);

        Pessoa pessoa = new Pessoa();
        pessoa.setCpf(data.cpf());
        pessoa.setNome(data.nome());
        pessoa.setEmail(data.email());
        pessoa.setCep(data.cep());
        pessoa.setTelefone(data.telefone());
        pessoa.setDataNascimento(data.dataNascimento());

        Cliente cliente = new Cliente();
        cliente.setUser(newUser);
        cliente.setPessoa(pessoa);
        clienteRepository.save(cliente);

        return ApiResponse.created("Usuário registrado com sucesso", null);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO data) {
        java.util.Optional<Pessoa> pessoaOpt = pessoaRepository.findByEmail(data.getEmail());
        if (pessoaOpt.isEmpty()) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "E-mail não encontrado no sistema", "Email not found");
        }
        return ApiResponse.success("Código de recuperação enviado (Use o código 123456 para testar)", null);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO data) {
        if (!"123456".equals(data.getCode())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Código de verificação inválido", "Invalid code");
        }

        Cliente cliente = clienteRepository.findByPessoaEmail(data.getEmail())
                .orElseThrow(() -> new RuntimeException("Nenhum cliente associado a este e-mail"));

        User user = cliente.getUser();
        if (user == null) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Nenhum usuário associado a este cliente", "User not found");
        }

        user.setPassword(passwordEncoder.encode(data.getNewPassword()));
        userRepository.save(user);

        return ApiResponse.success("Senha redefinida com sucesso", null);
    }

    @PostMapping("/alterar-senha")
    @org.springframework.security.access.prepost.PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> alterarSenha(@RequestBody @Valid com.cefet.VVVSystem.dto.AlterarSenhaRequestDTO data) {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        com.cefet.VVVSystem.security.MainUser mainUser = (com.cefet.VVVSystem.security.MainUser) auth.getPrincipal();
        User user = mainUser.getUser();

        if (!passwordEncoder.matches(data.getSenhaAtual(), user.getPassword())) {
            return ApiResponse.error(HttpStatus.BAD_REQUEST, "Senha atual incorreta", "Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(data.getNovaSenha()));
        userRepository.save(user);

        return ApiResponse.success("Senha alterada com sucesso", null);
    }
}
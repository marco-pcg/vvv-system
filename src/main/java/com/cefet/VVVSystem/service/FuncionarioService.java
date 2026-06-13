package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import com.cefet.VVVSystem.domain.repository.FuncionarioRepository;
import com.cefet.VVVSystem.dto.FuncionarioRequestDTO;
import com.cefet.VVVSystem.dto.FuncionarioResponseDTO;
import com.cefet.VVVSystem.exception.ConflictException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public FuncionarioResponseDTO criar(FuncionarioRequestDTO dto) {
        // Validação básica da matrícula já feita pelo DTO (@NotBlank),
        // mas aqui poderíamos adicionar regras complexas de negócio, como buscar se a matrícula já existe.
        if (funcionarioRepository.findByMatricula(dto.matricula()).isPresent()) {
            throw new ConflictException("Já existe um funcionário com esta matrícula");
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setCpf(dto.cpf());
        funcionario.setNome(dto.nome());
        funcionario.setCep(dto.cep());
        funcionario.setDataNascimento(dto.dataNascimento());
        funcionario.setMatricula(dto.matricula());
        funcionario.setEmail(dto.email());
        funcionario.setTelefone(dto.telefone());

        Funcionario saved = funcionarioRepository.save(funcionario);
        return new FuncionarioResponseDTO(saved);
    }

    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll().stream()
                .map(FuncionarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    public FuncionarioResponseDTO buscarPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário", "id", id));
        return new FuncionarioResponseDTO(funcionario);
    }

    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário", "id", id));
        
        // Verifica se a matrícula mudou e se a nova matrícula já existe
        if (!funcionario.getMatricula().equals(dto.matricula()) && 
            funcionarioRepository.findByMatricula(dto.matricula()).isPresent()) {
            throw new ConflictException("Já existe outro funcionário com esta matrícula");
        }

        funcionario.setCpf(dto.cpf());
        funcionario.setNome(dto.nome());
        funcionario.setCep(dto.cep());
        funcionario.setDataNascimento(dto.dataNascimento());
        funcionario.setMatricula(dto.matricula());
        funcionario.setEmail(dto.email());
        funcionario.setTelefone(dto.telefone());

        Funcionario updated = funcionarioRepository.save(funcionario);
        return new FuncionarioResponseDTO(updated);
    }

    public void excluir(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário", "id", id));
        funcionarioRepository.delete(funcionario);
    }
}

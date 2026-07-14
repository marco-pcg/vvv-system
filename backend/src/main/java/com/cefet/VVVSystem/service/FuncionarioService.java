package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import com.cefet.VVVSystem.domain.repository.FuncionarioRepository;
import com.cefet.VVVSystem.domain.entity.PontoDeVenda;
import com.cefet.VVVSystem.domain.repository.PontoDeVendaRepository;
import com.cefet.VVVSystem.dto.FuncionarioRequestDTO;
import com.cefet.VVVSystem.dto.FuncionarioResponseDTO;
import com.cefet.VVVSystem.exception.ConflictException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PontoDeVendaRepository pontoDeVendaRepository;

    @Autowired
    private com.cefet.VVVSystem.domain.repository.PessoaRepository pessoaRepository;

    public FuncionarioResponseDTO criar(FuncionarioRequestDTO dto) {
        if (funcionarioRepository.findByMatricula(dto.matricula()).isPresent()) {
            throw new ConflictException("Já existe um funcionário com esta matrícula");
        }

        if (funcionarioRepository.findByPessoaCpf(dto.cpf()).isPresent()) {
            throw new ConflictException("Este CPF já está cadastrado como funcionário");
        }

        com.cefet.VVVSystem.domain.entity.Pessoa pessoa = pessoaRepository.findByCpf(dto.cpf()).orElse(new com.cefet.VVVSystem.domain.entity.Pessoa());
        pessoa.setCpf(dto.cpf());
        pessoa.setNome(dto.nome());
        pessoa.setCep(dto.cep());
        pessoa.setDataNascimento(dto.dataNascimento());
        pessoa.setEmail(dto.email());
        pessoa.setTelefone(dto.telefone());

        Funcionario funcionario = new Funcionario();
        funcionario.setPessoa(pessoa);
        funcionario.setMatricula(dto.matricula());

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
        
        if (!funcionario.getMatricula().equals(dto.matricula()) && 
            funcionarioRepository.findByMatricula(dto.matricula()).isPresent()) {
            throw new ConflictException("Já existe outro funcionário com esta matrícula");
        }

        com.cefet.VVVSystem.domain.entity.Pessoa pessoa = funcionario.getPessoa();
        pessoa.setNome(dto.nome());
        pessoa.setCep(dto.cep());
        pessoa.setDataNascimento(dto.dataNascimento());
        pessoa.setEmail(dto.email());
        pessoa.setTelefone(dto.telefone());

        funcionario.setMatricula(dto.matricula());

        Funcionario updated = funcionarioRepository.save(funcionario);
        return new FuncionarioResponseDTO(updated);
    }

    public void excluir(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário", "id", id));
        funcionarioRepository.delete(funcionario);
    }

    public FuncionarioResponseDTO alocarPontoDeVenda(Long idFuncionario, Long idPdv) {
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário", "id", idFuncionario));

        PontoDeVenda pdv = pontoDeVendaRepository.findById(idPdv)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de Venda", "id", idPdv));

        funcionario.autorizarEmPontoDeVenda(pdv);

        Funcionario updated = funcionarioRepository.save(funcionario);
        return new FuncionarioResponseDTO(updated);
    }
}

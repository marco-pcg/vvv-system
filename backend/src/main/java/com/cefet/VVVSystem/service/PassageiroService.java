package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Passageiro;
import com.cefet.VVVSystem.domain.repository.PassageiroRepository;
import com.cefet.VVVSystem.dto.PassageiroRequestDTO;
import com.cefet.VVVSystem.dto.PassageiroResponseDTO;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import com.cefet.VVVSystem.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.cefet.VVVSystem.domain.entity.Pessoa;
import com.cefet.VVVSystem.domain.repository.PessoaRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassageiroService {

    @Autowired
    private PassageiroRepository passageiroRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public PassageiroResponseDTO criar(PassageiroRequestDTO dto) {
        if (passageiroRepository.findByPessoaCpf(dto.cpf()).isPresent()) {
            throw new ConflictException("Este CPF já está cadastrado como passageiro");
        }

        Pessoa pessoa = pessoaRepository.findByCpf(dto.cpf()).orElse(new Pessoa());
        pessoa.setCpf(dto.cpf());
        pessoa.setNome(dto.nome());
        pessoa.setCep(dto.cep());
        pessoa.setDataNascimento(dto.dataNascimento());
        pessoa.setEmail(dto.email());
        pessoa.setTelefone(dto.telefone());

        Passageiro passageiro = new Passageiro();
        passageiro.setPessoa(pessoa);
        passageiro.setPossuiAcompanhante(dto.possuiAcompanhante());

        Passageiro saved = passageiroRepository.save(passageiro);
        return new PassageiroResponseDTO(saved);
    }

    public List<PassageiroResponseDTO> listarTodos() {
        return passageiroRepository.findAll().stream()
                .map(PassageiroResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PassageiroResponseDTO buscarPorId(Long id) {
        Passageiro passageiro = passageiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro", "id", id));
        return new PassageiroResponseDTO(passageiro);
    }

    @Transactional
    public PassageiroResponseDTO atualizar(Long id, PassageiroRequestDTO dto) {
        Passageiro passageiro = passageiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro", "id", id));
        
        Pessoa pessoa = passageiro.getPessoa();
        // Não permitimos trocar o CPF de uma pessoa existente na atualização deste endpoint
        // mas atualizamos os outros dados
        pessoa.setNome(dto.nome());
        pessoa.setCep(dto.cep());
        pessoa.setDataNascimento(dto.dataNascimento());
        pessoa.setEmail(dto.email());
        pessoa.setTelefone(dto.telefone());
        
        passageiro.setPossuiAcompanhante(dto.possuiAcompanhante());

        Passageiro updated = passageiroRepository.save(passageiro);
        return new PassageiroResponseDTO(updated);
    }

    @Transactional
    public void excluir(Long id) {
        Passageiro passageiro = passageiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro", "id", id));
        passageiroRepository.delete(passageiro);
    }
}

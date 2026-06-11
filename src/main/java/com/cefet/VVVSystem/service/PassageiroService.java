package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Passageiro;
import com.cefet.VVVSystem.domain.repository.PassageiroRepository;
import com.cefet.VVVSystem.dto.PassageiroRequestDTO;
import com.cefet.VVVSystem.dto.PassageiroResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassageiroService {

    @Autowired
    private PassageiroRepository passageiroRepository;

    public PassageiroResponseDTO criar(PassageiroRequestDTO dto) {
        Passageiro passageiro = new Passageiro();
        passageiro.setCpf(dto.cpf());
        passageiro.setNome(dto.nome());
        passageiro.setCep(dto.cep());
        passageiro.setDataNascimento(dto.dataNascimento());
        passageiro.setEmail(dto.email());
        passageiro.setTelefone(dto.telefone());
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
                .orElseThrow(() -> new RuntimeException("Passageiro não encontrado"));
        return new PassageiroResponseDTO(passageiro);
    }

    public PassageiroResponseDTO atualizar(Long id, PassageiroRequestDTO dto) {
        Passageiro passageiro = passageiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passageiro não encontrado"));
        
        passageiro.setCpf(dto.cpf());
        passageiro.setNome(dto.nome());
        passageiro.setCep(dto.cep());
        passageiro.setDataNascimento(dto.dataNascimento());
        passageiro.setEmail(dto.email());
        passageiro.setTelefone(dto.telefone());
        passageiro.setPossuiAcompanhante(dto.possuiAcompanhante());

        Passageiro updated = passageiroRepository.save(passageiro);
        return new PassageiroResponseDTO(updated);
    }

    public void excluir(Long id) {
        Passageiro passageiro = passageiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passageiro não encontrado"));
        passageiroRepository.delete(passageiro);
    }
}

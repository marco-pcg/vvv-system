package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import com.cefet.VVVSystem.domain.entity.PontoDeVenda;
import com.cefet.VVVSystem.domain.repository.FuncionarioRepository;
import com.cefet.VVVSystem.domain.repository.PontoDeVendaRepository;
import com.cefet.VVVSystem.dto.PontoDeVendaRequestDTO;
import com.cefet.VVVSystem.dto.PontoDeVendaResponseDTO;
import com.cefet.VVVSystem.exception.ConflictException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PontoDeVendaService {

    @Autowired
    private PontoDeVendaRepository pontoDeVendaRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public PontoDeVendaResponseDTO criar(PontoDeVendaRequestDTO dto) {
        if (pontoDeVendaRepository.findByCnpj(dto.cnpj()).isPresent()) {
            throw new ConflictException("Já existe um Ponto de Venda com este CNPJ");
        }

        Funcionario gerente = funcionarioRepository.findById(dto.gerenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Gerente (Funcionario)", "id", dto.gerenteId()));

        PontoDeVenda pdv = new PontoDeVenda();
        pdv.setCnpj(dto.cnpj());
        pdv.setEndereco(dto.endereco());
        pdv.setGerente(gerente);

        PontoDeVenda saved = pontoDeVendaRepository.save(pdv);
        return new PontoDeVendaResponseDTO(saved);
    }

    public List<PontoDeVendaResponseDTO> listarTodos() {
        return pontoDeVendaRepository.findAll().stream()
                .map(PontoDeVendaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PontoDeVendaResponseDTO buscarPorId(Long id) {
        PontoDeVenda pdv = pontoDeVendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de Venda", "id", id));
        return new PontoDeVendaResponseDTO(pdv);
    }

    public PontoDeVendaResponseDTO atualizar(Long id, PontoDeVendaRequestDTO dto) {
        PontoDeVenda pdv = pontoDeVendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de Venda", "id", id));

        if (!pdv.getCnpj().equals(dto.cnpj()) && 
            pontoDeVendaRepository.findByCnpj(dto.cnpj()).isPresent()) {
            throw new ConflictException("Já existe outro Ponto de Venda com este CNPJ");
        }

        Funcionario gerente = funcionarioRepository.findById(dto.gerenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Gerente (Funcionario)", "id", dto.gerenteId()));

        pdv.setCnpj(dto.cnpj());
        pdv.setEndereco(dto.endereco());
        pdv.setGerente(gerente);

        PontoDeVenda updated = pontoDeVendaRepository.save(pdv);
        return new PontoDeVendaResponseDTO(updated);
    }

    public void excluir(Long id) {
        PontoDeVenda pdv = pontoDeVendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de Venda", "id", id));
        pontoDeVendaRepository.delete(pdv);
    }
}

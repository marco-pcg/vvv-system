package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Transportadora;
import com.cefet.VVVSystem.domain.repository.TransportadoraRepository;
import com.cefet.VVVSystem.dto.TransportadoraRequestDTO;
import com.cefet.VVVSystem.dto.TransportadoraResponseDTO;
import com.cefet.VVVSystem.exception.ConflictException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportadoraService {

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    public TransportadoraResponseDTO criar(TransportadoraRequestDTO dto) {
        String cnpjNumeros = dto.cnpj().replaceAll("[^0-9]", "");

        if (transportadoraRepository.findByCnpj(cnpjNumeros).isPresent()) {
            throw new ConflictException("Já existe uma Transportadora com este CNPJ");
        }

        Transportadora transportadora = new Transportadora();
        transportadora.setNome(dto.nome());
        transportadora.setCnpj(cnpjNumeros);

        Transportadora saved = transportadoraRepository.save(transportadora);
        return new TransportadoraResponseDTO(saved);
    }

    public List<TransportadoraResponseDTO> listarTodos() {
        return transportadoraRepository.findAll().stream()
                .map(TransportadoraResponseDTO::new)
                .collect(Collectors.toList());
    }

    public TransportadoraResponseDTO buscarPorId(Long id) {
        Transportadora transportadora = transportadoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora", "id", id));
        return new TransportadoraResponseDTO(transportadora);
    }

    public TransportadoraResponseDTO atualizar(Long id, TransportadoraRequestDTO dto) {
        Transportadora transportadora = transportadoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora", "id", id));

        String cnpjNumeros = dto.cnpj().replaceAll("[^0-9]", "");

        if (!transportadora.getCnpj().equals(cnpjNumeros) &&
            transportadoraRepository.findByCnpj(cnpjNumeros).isPresent()) {
            throw new ConflictException("Já existe outra Transportadora com este CNPJ");
        }

        transportadora.setNome(dto.nome());
        transportadora.setCnpj(cnpjNumeros);

        Transportadora updated = transportadoraRepository.save(transportadora);
        return new TransportadoraResponseDTO(updated);
    }

    public void excluir(Long id) {
        Transportadora transportadora = transportadoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora", "id", id));
        transportadoraRepository.delete(transportadora);
    }
}

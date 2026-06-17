package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Cliente;
import com.cefet.VVVSystem.domain.repository.ClienteRepository;
import com.cefet.VVVSystem.dto.ClienteResponseDTO;
import com.cefet.VVVSystem.dto.ClienteUpdateDTO;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public ClienteResponseDTO getMe(Long userId) {
        Cliente cliente = clienteRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o usuário atual"));
        
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setCpf(cliente.getCpf());
        dto.setNome(cliente.getNome());
        dto.setCep(cliente.getCep());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        return dto;
    }

    @Transactional
    public ClienteResponseDTO updateMe(Long userId, ClienteUpdateDTO dto) {
        Cliente cliente = clienteRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o usuário atual"));
        
        if (dto.getNome() != null) cliente.setNome(dto.getNome());
        if (dto.getCep() != null) cliente.setCep(dto.getCep());
        if (dto.getTelefone() != null) cliente.setTelefone(dto.getTelefone());
        
        clienteRepository.save(cliente);
        return getMe(userId);
    }
}

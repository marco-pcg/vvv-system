package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.entity.Transportadora;
import com.cefet.VVVSystem.domain.repository.ModalRepository;
import com.cefet.VVVSystem.domain.repository.TransportadoraRepository;
import com.cefet.VVVSystem.dto.ModalRequestDTO;
import com.cefet.VVVSystem.dto.ModalResponseDTO;
import com.cefet.VVVSystem.exception.ConflictException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModalService {

    @Autowired
    private ModalRepository modalRepository;

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    public ModalResponseDTO criar(ModalRequestDTO dto) {
        // Passo 2 e 3 do Diagrama de Sequência: verificarExistenciaTransportadora
        Transportadora transportadora = transportadoraRepository.findById(dto.idTransportadora())
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora", "id", dto.idTransportadora()));

        // Validação de negócio: não duplicar código
        if (modalRepository.findByCodigo(dto.codigo()).isPresent()) {
            throw new ConflictException("Já existe um Modal com este código");
        }

        // Passos 4 e 5: new Modal()
        Modal modal = new Modal();
        modal.setTransportadora(transportadora);
        modal.setCodigo(dto.codigo());
        modal.setTipo(dto.tipo());
        modal.setCapacidade(dto.capacidade());
        modal.setStatusOperacional(dto.statusOperacional());

        Modal saved = modalRepository.save(modal);
        return new ModalResponseDTO(saved);
    }

    public List<ModalResponseDTO> listarTodos() {
        return modalRepository.findAll().stream()
                .map(ModalResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ModalResponseDTO buscarPorId(Long id) {
        Modal modal = modalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Modal", "id", id));
        return new ModalResponseDTO(modal);
    }

    public ModalResponseDTO atualizar(Long id, ModalRequestDTO dto) {
        Modal modal = modalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Modal", "id", id));

        if (!modal.getCodigo().equals(dto.codigo()) &&
            modalRepository.findByCodigo(dto.codigo()).isPresent()) {
            throw new ConflictException("Já existe outro Modal com este código");
        }

        Transportadora transportadora = transportadoraRepository.findById(dto.idTransportadora())
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora", "id", dto.idTransportadora()));

        modal.setTransportadora(transportadora);
        modal.setCodigo(dto.codigo());
        modal.setTipo(dto.tipo());
        modal.setCapacidade(dto.capacidade());
        modal.setStatusOperacional(dto.statusOperacional());

        Modal updated = modalRepository.save(modal);
        return new ModalResponseDTO(updated);
    }

    public void excluir(Long id) {
        Modal modal = modalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Modal", "id", id));
        modalRepository.delete(modal);
    }
}

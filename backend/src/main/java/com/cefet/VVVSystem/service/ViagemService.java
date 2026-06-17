package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Cidade;
import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.entity.Viagem;
import com.cefet.VVVSystem.domain.enums.StatusOperacional;
import com.cefet.VVVSystem.domain.enums.StatusViagem;
import com.cefet.VVVSystem.domain.repository.CidadeRepository;
import com.cefet.VVVSystem.domain.repository.ModalRepository;
import com.cefet.VVVSystem.domain.repository.ViagemRepository;
import com.cefet.VVVSystem.dto.ViagemRequestDTO;
import com.cefet.VVVSystem.dto.ViagemResponseDTO;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import com.cefet.VVVSystem.mapper.ViagemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViagemService {

    private final ViagemRepository viagemRepository;
    private final ModalRepository modalRepository;
    private final CidadeRepository cidadeRepository;
    private final ViagemMapper viagemMapper;

    @Transactional
    public ViagemResponseDTO create(ViagemRequestDTO dto) {
        java.util.Set<Modal> modais = new java.util.HashSet<>(modalRepository.findAllById(dto.getIdsModais()));
        if (modais.isEmpty() || modais.size() != dto.getIdsModais().size()) {
            throw new ResourceNotFoundException("Um ou mais Modais não foram encontrados.");
        }
        for (Modal m : modais) {
            if (m.getStatusOperacional() != StatusOperacional.OPERACIONAL) {
                throw new BusinessException("O Modal " + m.getCodigo() + " não está operacional.");
            }
        }
        
        java.util.Set<Cidade> escalas = new java.util.HashSet<>();
        if (dto.getIdsEscalas() != null && !dto.getIdsEscalas().isEmpty()) {
            escalas.addAll(cidadeRepository.findAllById(dto.getIdsEscalas()));
            if (escalas.size() != dto.getIdsEscalas().size()) {
                throw new ResourceNotFoundException("Uma ou mais Cidades de escala não foram encontradas.");
            }
        }

        Cidade cidadeOrigem = cidadeRepository.findById(dto.getIdCidadeOrigem())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade de origem não encontrada com o ID: " + dto.getIdCidadeOrigem()));

        Cidade cidadeDestino = cidadeRepository.findById(dto.getIdCidadeDestino())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade de destino não encontrada com o ID: " + dto.getIdCidadeDestino()));

        if (cidadeOrigem.getId().equals(cidadeDestino.getId())) {
            throw new BusinessException("A cidade de origem não pode ser a mesma de destino.");
        }

        if (!dto.getPartida().isBefore(dto.getChegada())) {
            throw new BusinessException("A data de partida deve ser anterior à data de chegada.");
        }

        // Validação de Conflito de Horário para os Modais (Impede agendar o mesmo ônibus para duas viagens simultâneas)
        long conflitos = viagemRepository.countViagensConflitantes(
                new java.util.ArrayList<>(dto.getIdsModais()), dto.getPartida(), dto.getChegada()
        );
        if (conflitos > 0) {
            throw new BusinessException("Um ou mais veículos já estão ocupados em outra viagem nesse mesmo horário.");
        }

        Viagem viagem = viagemMapper.toEntity(dto, modais, cidadeOrigem, cidadeDestino, escalas);
        viagem.setStatus(StatusViagem.AGENDADA);

        viagem = viagemRepository.save(viagem);

        return viagemMapper.toResponseDTO(viagem);
    }

    @Transactional(readOnly = true)
    public List<ViagemResponseDTO> findAll() {
        return viagemRepository.findAll().stream()
                .map(viagemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ViagemResponseDTO findById(Long id) {
        Viagem viagem = getViagemById(id);
        return viagemMapper.toResponseDTO(viagem);
    }

    @Transactional
    public ViagemResponseDTO update(Long id, ViagemRequestDTO dto) {
        Viagem viagem = getViagemById(id);

        java.util.Set<Modal> modais = new java.util.HashSet<>(modalRepository.findAllById(dto.getIdsModais()));
        if (modais.isEmpty() || modais.size() != dto.getIdsModais().size()) {
            throw new ResourceNotFoundException("Um ou mais Modais não foram encontrados.");
        }
        for (Modal m : modais) {
            if (m.getStatusOperacional() != StatusOperacional.OPERACIONAL) {
                throw new BusinessException("O Modal " + m.getCodigo() + " não está operacional.");
            }
        }
        
        java.util.Set<Cidade> escalas = new java.util.HashSet<>();
        if (dto.getIdsEscalas() != null && !dto.getIdsEscalas().isEmpty()) {
            escalas.addAll(cidadeRepository.findAllById(dto.getIdsEscalas()));
            if (escalas.size() != dto.getIdsEscalas().size()) {
                throw new ResourceNotFoundException("Uma ou mais Cidades de escala não foram encontradas.");
            }
        }

        Cidade cidadeOrigem = cidadeRepository.findById(dto.getIdCidadeOrigem())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade de origem não encontrada com o ID: " + dto.getIdCidadeOrigem()));

        Cidade cidadeDestino = cidadeRepository.findById(dto.getIdCidadeDestino())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade de destino não encontrada com o ID: " + dto.getIdCidadeDestino()));

        if (cidadeOrigem.getId().equals(cidadeDestino.getId())) {
            throw new BusinessException("A cidade de origem não pode ser a mesma de destino.");
        }

        if (!dto.getPartida().isBefore(dto.getChegada())) {
            throw new BusinessException("A data de partida deve ser anterior à data de chegada.");
        }

        viagem.setModais(modais);
        viagem.setCidadeOrigem(cidadeOrigem);
        viagem.setCidadeDestino(cidadeDestino);
        viagem.setEscalas(escalas);
        viagem.setPartida(dto.getPartida());
        viagem.setChegada(dto.getChegada());
        viagem.setPreco(dto.getPreco());

        viagem = viagemRepository.save(viagem);
        return viagemMapper.toResponseDTO(viagem);
    }

    @Transactional
    public void delete(Long id) {
        Viagem viagem = getViagemById(id);
        viagemRepository.delete(viagem);
    }

    private Viagem getViagemById(Long id) {
        return viagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada com o ID: " + id));
    }
}

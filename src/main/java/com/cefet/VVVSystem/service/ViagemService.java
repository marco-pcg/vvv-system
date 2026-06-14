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
        Modal modal = modalRepository.findById(dto.getIdModal())
                .orElseThrow(() -> new ResourceNotFoundException("Modal não encontrado com o ID: " + dto.getIdModal()));

        if (modal.getStatusOperacional() != StatusOperacional.OPERACIONAL) {
            throw new BusinessException("O Modal selecionado não está operacional.");
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

        Viagem viagem = viagemMapper.toEntity(dto, modal, cidadeOrigem, cidadeDestino);
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

        Modal modal = modalRepository.findById(dto.getIdModal())
                .orElseThrow(() -> new ResourceNotFoundException("Modal não encontrado com o ID: " + dto.getIdModal()));

        if (modal.getStatusOperacional() != StatusOperacional.OPERACIONAL) {
            throw new BusinessException("O Modal selecionado não está operacional.");
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

        viagem.setModal(modal);
        viagem.setCidadeOrigem(cidadeOrigem);
        viagem.setCidadeDestino(cidadeDestino);
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

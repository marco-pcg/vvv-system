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
import com.cefet.VVVSystem.mapper.ViagemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViagemServiceTest {

    @Mock
    private ViagemRepository viagemRepository;

    @Mock
    private ModalRepository modalRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private ViagemMapper viagemMapper;

    @InjectMocks
    private ViagemService viagemService;

    private ViagemRequestDTO requestDTO;
    private Modal modal;
    private Cidade cidadeOrigem;
    private Cidade cidadeDestino;

    @BeforeEach
    void setUp() {
        requestDTO = new ViagemRequestDTO();
        requestDTO.setIdsModais(java.util.List.of(1L));
        requestDTO.setIdsEscalas(java.util.List.of());
        requestDTO.setIdCidadeOrigem(1L);
        requestDTO.setIdCidadeDestino(2L);
        requestDTO.setPartida(LocalDateTime.now().plusDays(1));
        requestDTO.setChegada(LocalDateTime.now().plusDays(2));
        requestDTO.setPreco(new BigDecimal("100.00"));

        modal = new Modal();
        modal.setId(1L);
        modal.setStatusOperacional(StatusOperacional.OPERACIONAL);

        cidadeOrigem = new Cidade();
        cidadeOrigem.setId(1L);

        cidadeDestino = new Cidade();
        cidadeDestino.setId(2L);
    }

    @Test
    void createViagem_Success() {
        when(modalRepository.findAllById(any())).thenReturn(java.util.List.of(modal));
        when(cidadeRepository.findById(1L)).thenReturn(Optional.of(cidadeOrigem));
        when(cidadeRepository.findById(2L)).thenReturn(Optional.of(cidadeDestino));

        Viagem viagemEntity = new Viagem();
        when(viagemMapper.toEntity(any(), any(), any(), any(), any())).thenReturn(viagemEntity);
        when(viagemRepository.save(any(Viagem.class))).thenReturn(viagemEntity);

        ViagemResponseDTO responseDTO = new ViagemResponseDTO();
        responseDTO.setId(1L);
        when(viagemMapper.toResponseDTO(any())).thenReturn(responseDTO);

        ViagemResponseDTO result = viagemService.create(requestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(viagemRepository, times(1)).save(viagemEntity);
    }

    @Test
    void createViagem_ModalNotOperacional_ThrowsException() {
        modal.setStatusOperacional(StatusOperacional.INATIVO);
        when(modalRepository.findAllById(any())).thenReturn(java.util.List.of(modal));

        assertThrows(BusinessException.class, () -> viagemService.create(requestDTO));
        verify(viagemRepository, never()).save(any());
    }

    @Test
    void createViagem_SameCidadeOrigemAndDestino_ThrowsException() {
        requestDTO.setIdCidadeDestino(1L);
        when(modalRepository.findAllById(any())).thenReturn(java.util.List.of(modal));
        when(cidadeRepository.findById(1L)).thenReturn(Optional.of(cidadeOrigem));

        assertThrows(BusinessException.class, () -> viagemService.create(requestDTO));
        verify(viagemRepository, never()).save(any());
    }

    @Test
    void createViagem_PartidaAfterChegada_ThrowsException() {
        requestDTO.setPartida(LocalDateTime.now().plusDays(3));
        when(modalRepository.findAllById(any())).thenReturn(java.util.List.of(modal));
        when(cidadeRepository.findById(1L)).thenReturn(Optional.of(cidadeOrigem));
        when(cidadeRepository.findById(2L)).thenReturn(Optional.of(cidadeDestino));

        assertThrows(BusinessException.class, () -> viagemService.create(requestDTO));
        verify(viagemRepository, never()).save(any());
    }
}

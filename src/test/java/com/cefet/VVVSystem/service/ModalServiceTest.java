package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.entity.Transportadora;
import com.cefet.VVVSystem.domain.enums.StatusOperacional;
import com.cefet.VVVSystem.domain.enums.TipoModal;
import com.cefet.VVVSystem.domain.repository.ModalRepository;
import com.cefet.VVVSystem.domain.repository.TransportadoraRepository;
import com.cefet.VVVSystem.dto.ModalRequestDTO;
import com.cefet.VVVSystem.dto.ModalResponseDTO;
import com.cefet.VVVSystem.exception.ConflictException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModalServiceTest {

    @Mock
    private ModalRepository modalRepository;

    @Mock
    private TransportadoraRepository transportadoraRepository;

    @InjectMocks
    private ModalService modalService;

    private ModalRequestDTO requestDTO;
    private Transportadora transportadora;
    private Modal modal;

    @BeforeEach
    void setUp() {
        requestDTO = new ModalRequestDTO(1L, "O-1002", TipoModal.ONIBUS, 42, StatusOperacional.OPERACIONAL);

        transportadora = new Transportadora();
        transportadora.setId(1L);
        transportadora.setNome("Viação Teste");

        modal = new Modal();
        modal.setId(10L);
        modal.setTransportadora(transportadora);
        modal.setCodigo("O-1002");
        modal.setTipo(TipoModal.ONIBUS);
        modal.setCapacidade(42);
        modal.setStatusOperacional(StatusOperacional.OPERACIONAL);
    }

    @Test
    @DisplayName("Deve criar um Modal com sucesso quando a Transportadora existir e os dados forem válidos")
    void criarModalComSucesso() {
        // Arrange
        // Passo 2 do diagrama: verificarExistenciaTransportadora
        when(transportadoraRepository.findById(requestDTO.idTransportadora())).thenReturn(Optional.of(transportadora));
        // Validação de negócio
        when(modalRepository.findByCodigo(requestDTO.codigo())).thenReturn(Optional.empty());
        // Passo 4: criar Modal
        when(modalRepository.save(any(Modal.class))).thenReturn(modal);

        // Act
        ModalResponseDTO response = modalService.criar(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(modal.getId(), response.id());
        assertEquals(requestDTO.codigo(), response.codigo());
        assertEquals(requestDTO.idTransportadora(), response.idTransportadora());

        // Verificações que comprovam a sequência do diagrama
        verify(transportadoraRepository, times(1)).findById(requestDTO.idTransportadora());
        verify(modalRepository, times(1)).findByCodigo(requestDTO.codigo());
        verify(modalRepository, times(1)).save(any(Modal.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar criar Modal quando a Transportadora não existe")
    void criarModalTransportadoraNaoEncontrada() {
        // Arrange
        // Passo 2 e 3 do diagrama: falha na verificação da transportadora
        when(transportadoraRepository.findById(requestDTO.idTransportadora())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> modalService.criar(requestDTO));

        // Verificações
        verify(transportadoraRepository, times(1)).findById(requestDTO.idTransportadora());
        verify(modalRepository, never()).findByCodigo(any());
        verify(modalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ConflictException ao tentar criar Modal com código já existente")
    void criarModalCodigoDuplicado() {
        // Arrange
        when(transportadoraRepository.findById(requestDTO.idTransportadora())).thenReturn(Optional.of(transportadora));
        when(modalRepository.findByCodigo(requestDTO.codigo())).thenReturn(Optional.of(modal));

        // Act & Assert
        assertThrows(ConflictException.class, () -> modalService.criar(requestDTO));

        // Verificações
        verify(transportadoraRepository, times(1)).findById(requestDTO.idTransportadora());
        verify(modalRepository, times(1)).findByCodigo(requestDTO.codigo());
        verify(modalRepository, never()).save(any());
    }
}

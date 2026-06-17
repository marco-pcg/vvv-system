package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Transportadora;
import com.cefet.VVVSystem.domain.repository.TransportadoraRepository;
import com.cefet.VVVSystem.dto.TransportadoraRequestDTO;
import com.cefet.VVVSystem.dto.TransportadoraResponseDTO;
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
class TransportadoraServiceTest {

    @Mock
    private TransportadoraRepository transportadoraRepository;

    @InjectMocks
    private TransportadoraService transportadoraService;

    private TransportadoraRequestDTO requestDTO;
    private Transportadora transportadora;

    @BeforeEach
    void setUp() {
        requestDTO = new TransportadoraRequestDTO("Viação Teste", "12.345.678/0001-99");

        transportadora = new Transportadora();
        transportadora.setId(1L);
        transportadora.setNome("Viação Teste");
        transportadora.setCnpj("12345678000199");
    }

    @Test
    @DisplayName("Deve criar Transportadora com sucesso quando o CNPJ for válido e não existir")
    void criarTransportadoraComSucesso() {
        when(transportadoraRepository.findByCnpj("12345678000199")).thenReturn(Optional.empty());
        when(transportadoraRepository.save(any(Transportadora.class))).thenReturn(transportadora);

        TransportadoraResponseDTO response = transportadoraService.criar(requestDTO);

        assertNotNull(response);
        assertEquals(transportadora.getId(), response.id());
        assertEquals(requestDTO.nome(), response.nome());
        assertEquals("12345678000199", response.cnpj());

        verify(transportadoraRepository, times(1)).findByCnpj("12345678000199");
        verify(transportadoraRepository, times(1)).save(any(Transportadora.class));
    }

    @Test
    @DisplayName("Deve lançar ConflictException ao criar Transportadora com CNPJ já existente")
    void criarTransportadoraCnpjDuplicado() {
        when(transportadoraRepository.findByCnpj("12345678000199")).thenReturn(Optional.of(transportadora));

        assertThrows(ConflictException.class, () -> transportadoraService.criar(requestDTO));

        verify(transportadoraRepository, times(1)).findByCnpj("12345678000199");
        verify(transportadoraRepository, never()).save(any(Transportadora.class));
    }

    @Test
    @DisplayName("Deve buscar Transportadora por ID com sucesso")
    void buscarTransportadoraPorIdComSucesso() {
        when(transportadoraRepository.findById(1L)).thenReturn(Optional.of(transportadora));

        TransportadoraResponseDTO response = transportadoraService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(transportadoraRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void buscarTransportadoraPorIdNaoEncontrado() {
        when(transportadoraRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transportadoraService.buscarPorId(1L));

        verify(transportadoraRepository, times(1)).findById(1L);
    }
}

package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.*;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.repository.ClienteRepository;
import com.cefet.VVVSystem.domain.repository.PassageiroRepository;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import com.cefet.VVVSystem.domain.repository.ViagemRepository;
import com.cefet.VVVSystem.dto.ReservaRequestDTO;
import com.cefet.VVVSystem.dto.ReservaResponseDTO;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.mapper.ReservaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ViagemRepository viagemRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private PassageiroRepository passageiroRepository;
    @Mock
    private DescontoService descontoService;
    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaService reservaService;

    private ReservaRequestDTO requestDTO;
    private Viagem viagem;
    private Cliente cliente;
    private Passageiro passageiro;
    private Modal modal;

    @BeforeEach
    void setUp() {
        requestDTO = new ReservaRequestDTO();
        requestDTO.setIdViagem(1L);
        requestDTO.setIdCliente(1L);
        requestDTO.setIdPassageiro(1L);

        modal = new Modal();
        modal.setId(1L);
        modal.setCapacidade(50);

        viagem = new Viagem();
        viagem.setId(1L);
        viagem.setModais(new java.util.HashSet<>(java.util.List.of(modal)));
        viagem.setPreco(new BigDecimal("200.00"));
        viagem.setPartida(LocalDateTime.of(2026, 8, 1, 10, 0));

        cliente = new Cliente();
        cliente.setId(1L);

        passageiro = new Passageiro();
        passageiro.setId(1L);
        passageiro.setDataNascimento(LocalDate.of(2000, 1, 1)); // adulto
    }

    @Test
    void create_Success_AdultoSemDesconto() {
        when(viagemRepository.findById(1L)).thenReturn(Optional.of(viagem));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passageiroRepository.findById(1L)).thenReturn(Optional.of(passageiro));
        when(reservaRepository.countByViagemIdAndStatusIn(eq(1L), anyList())).thenReturn(0L);
        when(descontoService.calcularPrecoComDesconto(any(), any(), any()))
                .thenReturn(new BigDecimal("200.00"));

        Reserva reservaSalva = new Reserva();
        reservaSalva.setId(1L);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);

        ReservaResponseDTO responseDTO = new ReservaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setValorTotal(new BigDecimal("200.00"));
        when(reservaMapper.toResponseDTO(any())).thenReturn(responseDTO);

        ReservaResponseDTO result = reservaService.create(requestDTO);

        assertNotNull(result);
        assertEquals(new BigDecimal("200.00"), result.getValorTotal());
        verify(descontoService).calcularPrecoComDesconto(any(), eq(passageiro), any());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void create_Success_CriancaComDesconto() {
        passageiro.setDataNascimento(LocalDate.of(2018, 1, 1)); // criança ~8 anos

        when(viagemRepository.findById(1L)).thenReturn(Optional.of(viagem));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passageiroRepository.findById(1L)).thenReturn(Optional.of(passageiro));
        when(reservaRepository.countByViagemIdAndStatusIn(eq(1L), anyList())).thenReturn(0L);
        when(descontoService.calcularPrecoComDesconto(any(), any(), any()))
                .thenReturn(new BigDecimal("100.00")); // 50% de desconto

        Reserva reservaSalva = new Reserva();
        reservaSalva.setId(1L);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);

        ReservaResponseDTO responseDTO = new ReservaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setValorTotal(new BigDecimal("100.00"));
        when(reservaMapper.toResponseDTO(any())).thenReturn(responseDTO);

        ReservaResponseDTO result = reservaService.create(requestDTO);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.getValorTotal());
        verify(descontoService).calcularPrecoComDesconto(
                eq(new BigDecimal("200.00")), eq(passageiro), any());
    }

    @Test
    void create_Overbooking_ThrowsException() {
        when(viagemRepository.findById(1L)).thenReturn(Optional.of(viagem));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passageiroRepository.findById(1L)).thenReturn(Optional.of(passageiro));
        // Capacidade cheia (50 reservas para modal com capacidade 50)
        when(reservaRepository.countByViagemIdAndStatusIn(eq(1L), anyList())).thenReturn(50L);

        assertThrows(BusinessException.class, () -> reservaService.create(requestDTO));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void create_ViagemNotFound_ThrowsException() {
        when(viagemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> reservaService.create(requestDTO));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void create_PassageiroNotFound_ThrowsException() {
        when(viagemRepository.findById(1L)).thenReturn(Optional.of(viagem));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(passageiroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> reservaService.create(requestDTO));
        verify(reservaRepository, never()).save(any());
    }
}

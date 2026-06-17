package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.*;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import com.cefet.VVVSystem.domain.repository.PagamentoRepository;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import com.cefet.VVVSystem.dto.ReservaRequestDTO;
import com.cefet.VVVSystem.dto.ReservaResponseDTO;
import com.cefet.VVVSystem.dto.VendaOnlineRequestDTO;
import com.cefet.VVVSystem.dto.VendaOnlineResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaOnlineServiceTest {

    @Mock
    private ReservaService reservaService;
    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ProcessadorPagamento processadorPagamento;
    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private VendaOnlineService vendaOnlineService;

    private VendaOnlineRequestDTO requestDTO;
    private Reserva reserva;
    private ReservaResponseDTO reservaResponseDTO;

    @BeforeEach
    void setUp() {
        ReservaRequestDTO reservaRequestDTO = new ReservaRequestDTO();
        reservaRequestDTO.setIdViagem(1L);

        requestDTO = new VendaOnlineRequestDTO();
        requestDTO.setReserva(reservaRequestDTO);
        requestDTO.setTipoPagamento(TipoPagamento.CREDITO);
        requestDTO.setNumeroCartao("1234123412341234");
        requestDTO.setParcelas(2);

        reservaResponseDTO = new ReservaResponseDTO();
        reservaResponseDTO.setId(10L);

        reserva = new Reserva();
        reserva.setId(10L);
        reserva.setCodigo("RES-1234");
        reserva.setStatus(StatusReserva.PENDENTE);
    }

    @Test
    void solicitarVenda_Sucesso() {
        when(reservaService.create(any(ReservaRequestDTO.class))).thenReturn(reservaResponseDTO);
        when(reservaRepository.findById(10L)).thenReturn(Optional.of(reserva));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(i -> i.getArguments()[0]);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArguments()[0]);

        VendaOnlineResponseDTO response = vendaOnlineService.solicitarVenda(requestDTO);

        assertNotNull(response);
        assertEquals(10L, response.getReservaId());
        
        verify(processadorPagamento).processarERegistrarPagamento(
            eq(reserva), 
            any(PagamentoCredito.class), 
            eq(StatusReserva.AGUARDANDO_APROVACAO)
        );
        verify(pagamentoRepository).save(any(PagamentoCredito.class));
        verify(reservaRepository).save(reserva);
    }
}

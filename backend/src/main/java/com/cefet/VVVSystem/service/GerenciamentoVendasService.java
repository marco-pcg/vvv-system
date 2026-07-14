package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.Ticket;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GerenciamentoVendasService {

    private final ReservaRepository reservaRepository;
    private final TicketService ticketService;

    @Transactional
    public Ticket aprovarVendaOnline(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada."));

        if (reserva.getStatus() != StatusReserva.AGUARDANDO_APROVACAO) {
            throw new BusinessException("A reserva não está aguardando aprovação. Status atual: " + reserva.getStatus());
        }

        reserva.setStatus(StatusReserva.CONFIRMADA);
        reserva = reservaRepository.save(reserva);

        return ticketService.emitirTicket(reserva);
    }

    @Transactional
    public void rejeitarVendaOnline(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada."));

        if (reserva.getStatus() != StatusReserva.AGUARDANDO_APROVACAO) {
            throw new BusinessException("A reserva não está aguardando aprovação. Status atual: " + reserva.getStatus());
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }
}

package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.Ticket;
import com.cefet.VVVSystem.domain.repository.TicketRepository;
import com.cefet.VVVSystem.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public Ticket emitirTicket(Reserva reserva) {
        if (ticketRepository.findByReservaId(reserva.getId()).isPresent()) {
            throw new BusinessException("Já existe um ticket emitido para esta reserva.");
        }

        Ticket ticket = new Ticket();
        ticket.setReserva(reserva);
        // Gera um número único para o ticket
        ticket.setNumero(gerarNumeroTicket());
        // Atribui assento aleatório ou fixo para efeito de MVP
        ticket.setAssento(gerarAssento(reserva));

        return ticketRepository.save(ticket);
    }

    private String gerarNumeroTicket() {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String gerarAssento(Reserva reserva) {
        // Simulação de alocação de assento
        long count = reserva.getId() % 50; // Para gerar assentos de 1 a 50
        char row = (char) ('A' + (count % 10));
        return row + String.valueOf((count / 10) + 1);
    }
}

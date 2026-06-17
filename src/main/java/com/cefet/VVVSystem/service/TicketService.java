package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.Ticket;
import com.cefet.VVVSystem.domain.repository.TicketRepository;
import com.cefet.VVVSystem.dto.TicketResponseDTO;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import com.cefet.VVVSystem.mapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Transactional
    public Ticket emitirTicket(Reserva reserva) {
        if (ticketRepository.findByReservaId(reserva.getId()).isPresent()) {
            throw new BusinessException("Já existe um ticket emitido para esta reserva.");
        }

        String numero = gerarNumeroTicket();
        String assento = gerarAssento(reserva);
        
        Ticket ticket = reserva.instanciarTicket(numero, assento);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public TicketResponseDTO findByNumero(String numero) {
        Ticket ticket = ticketRepository.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "numero", numero));
        return ticketMapper.toResponseDTO(ticket);
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

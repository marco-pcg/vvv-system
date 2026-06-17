package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Ticket;
import com.cefet.VVVSystem.domain.repository.TicketRepository;
import com.cefet.VVVSystem.dto.TicketResponseDTO;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import com.cefet.VVVSystem.mapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Transactional(readOnly = true)
    public TicketResponseDTO findByNumero(String numero) {
        Ticket ticket = ticketRepository.findByNumero(numero)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "numero", numero));
        return ticketMapper.toResponseDTO(ticket);
    }
}

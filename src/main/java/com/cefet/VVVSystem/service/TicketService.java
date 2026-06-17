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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import com.cefet.VVVSystem.domain.entity.User;

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            boolean isAdminOrFuncionario = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                            || a.getAuthority().equals("ROLE_GERENTE")
                            || a.getAuthority().equals("ROLE_FUNCIONARIO"));

            if (!isAdminOrFuncionario) {
                String currentUsername = auth.getName();
                User donoUser = ticket.getReserva().getCliente().getUser();
                if (donoUser == null || !currentUsername.equals(donoUser.getUsername())) {
                    throw new AccessDeniedException("Acesso negado: O ticket pertence a outro usuário.");
                }
            }
        }

        return ticketMapper.toResponseDTO(ticket);
    }

    @Transactional(readOnly = true)
    public java.util.List<TicketResponseDTO> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
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

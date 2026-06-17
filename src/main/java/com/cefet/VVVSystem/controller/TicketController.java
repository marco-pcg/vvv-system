package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.TicketResponseDTO;
import com.cefet.VVVSystem.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{numero}")
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_TICKET_EMIT, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_READ, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_SELF_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_MANAGE_ALL)")
    public ResponseEntity<TicketResponseDTO> consultarTicket(@PathVariable String numero) {
        return ResponseEntity.ok(ticketService.findByNumero(numero));
    }
}

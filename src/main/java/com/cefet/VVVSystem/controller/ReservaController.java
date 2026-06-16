package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.ReservaRequestDTO;
import com.cefet.VVVSystem.dto.ReservaResponseDTO;
import com.cefet.VVVSystem.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_CREATE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_SELF_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_MANAGE_ALL)")
    public ResponseEntity<ReservaResponseDTO> create(@Valid @RequestBody ReservaRequestDTO dto) {
        ReservaResponseDTO created = reservaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_READ, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_ONLINE_READ, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_SELF_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_MANAGE_ALL)")
    public ResponseEntity<List<ReservaResponseDTO>> findAll() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_READ, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_ONLINE_READ, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_SELF_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_MANAGE_ALL)")
    public ResponseEntity<ReservaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.findById(id));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_SELF_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_MANAGE_ALL)")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        reservaService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}

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

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> create(@Valid @RequestBody ReservaRequestDTO dto) {
        ReservaResponseDTO created = reservaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> findAll() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.findById(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        reservaService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}

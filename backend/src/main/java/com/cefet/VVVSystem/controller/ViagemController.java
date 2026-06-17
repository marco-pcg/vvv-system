package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.ViagemRequestDTO;
import com.cefet.VVVSystem.dto.ViagemResponseDTO;
import com.cefet.VVVSystem.service.ViagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/viagens")
@RequiredArgsConstructor
public class ViagemController {

    private final ViagemService viagemService;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_VIAGEM_MANAGE)")
    public ResponseEntity<ViagemResponseDTO> create(@Valid @RequestBody ViagemRequestDTO dto) {
        ViagemResponseDTO created = viagemService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_VIAGEM_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_VIAGEM_READ)")
    public ResponseEntity<List<ViagemResponseDTO>> findAll() {
        return ResponseEntity.ok(viagemService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_VIAGEM_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_VIAGEM_READ)")
    public ResponseEntity<ViagemResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(viagemService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_VIAGEM_MANAGE)")
    public ResponseEntity<ViagemResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ViagemRequestDTO dto) {
        return ResponseEntity.ok(viagemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_VIAGEM_MANAGE)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viagemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

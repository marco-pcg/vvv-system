package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.CidadeRequestDTO;
import com.cefet.VVVSystem.dto.CidadeResponseDTO;
import com.cefet.VVVSystem.service.CidadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    private final CidadeService cidadeService;

    public CidadeController(CidadeService cidadeService) {
        this.cidadeService = cidadeService;
    }

    @GetMapping
    public ResponseEntity<List<CidadeResponseDTO>> findAll() {
        return ResponseEntity.ok(cidadeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadeResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cidadeService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_CIDADE_MANAGE)")
    public ResponseEntity<CidadeResponseDTO> create(@RequestBody CidadeRequestDTO dto) {
        CidadeResponseDTO created = cidadeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_CIDADE_MANAGE)")
    public ResponseEntity<CidadeResponseDTO> update(@PathVariable Long id, @RequestBody CidadeRequestDTO dto) {
        return ResponseEntity.ok(cidadeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_CIDADE_MANAGE)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

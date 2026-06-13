package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.AeroportoDTO;
import com.cefet.VVVSystem.service.AeroportoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aeroportos")
public class AeroportoController {

    private final AeroportoService aeroportoService;

    public AeroportoController(AeroportoService aeroportoService) {
        this.aeroportoService = aeroportoService;
    }

    @GetMapping
    public ResponseEntity<List<AeroportoDTO>> findAll() {
        return ResponseEntity.ok(aeroportoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AeroportoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(aeroportoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AeroportoDTO> create(@RequestBody @Valid AeroportoDTO dto) {
        AeroportoDTO created = aeroportoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AeroportoDTO> update(@PathVariable Long id, @RequestBody @Valid AeroportoDTO dto) {
        return ResponseEntity.ok(aeroportoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        aeroportoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
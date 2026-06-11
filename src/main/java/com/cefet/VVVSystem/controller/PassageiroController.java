package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.PassageiroRequestDTO;
import com.cefet.VVVSystem.dto.PassageiroResponseDTO;
import com.cefet.VVVSystem.service.PassageiroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {

    @Autowired
    private PassageiroService passageiroService;

    @PostMapping
    public ResponseEntity<PassageiroResponseDTO> criar(@RequestBody @Valid PassageiroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passageiroService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<PassageiroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(passageiroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassageiroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(passageiroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassageiroResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PassageiroRequestDTO dto) {
        return ResponseEntity.ok(passageiroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        passageiroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

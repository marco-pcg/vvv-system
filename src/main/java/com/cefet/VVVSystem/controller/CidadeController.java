package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.CidadeDTO;
import com.cefet.VVVSystem.service.CidadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    private final CidadeService cidadeService;

    public CidadeController(CidadeService cidadeService) {
        this.cidadeService = cidadeService;
    }

    @GetMapping
    public ResponseEntity<List<CidadeDTO>> findAll() {
        return ResponseEntity.ok(cidadeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cidadeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CidadeDTO> create(@RequestBody CidadeDTO dto) {
        CidadeDTO created = cidadeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadeDTO> update(@PathVariable Long id, @RequestBody CidadeDTO dto) {
        return ResponseEntity.ok(cidadeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

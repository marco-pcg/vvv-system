package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.PassageiroRequestDTO;
import com.cefet.VVVSystem.dto.PassageiroResponseDTO;
import com.cefet.VVVSystem.response.ApiResponse;
import com.cefet.VVVSystem.service.PassageiroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {

    @Autowired
    private PassageiroService passageiroService;

    @PostMapping
    public ResponseEntity<ApiResponse<PassageiroResponseDTO>> criar(@RequestBody @Valid PassageiroRequestDTO dto) {
        return ApiResponse.created("Passageiro criado com sucesso", passageiroService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PassageiroResponseDTO>>> listarTodos() {
        return ApiResponse.success("Passageiros listados com sucesso", passageiroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PassageiroResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ApiResponse.success("Passageiro encontrado com sucesso", passageiroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PassageiroResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid PassageiroRequestDTO dto) {
        return ApiResponse.success("Passageiro atualizado com sucesso", passageiroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        passageiroService.excluir(id);
        return ApiResponse.success("Passageiro excluído com sucesso", null);
    }
}

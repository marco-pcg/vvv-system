package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.ModalRequestDTO;
import com.cefet.VVVSystem.dto.ModalResponseDTO;
import com.cefet.VVVSystem.response.ApiResponse;
import com.cefet.VVVSystem.service.ModalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modais")
public class ModalController {

    @Autowired
    private ModalService modalService;

    @PostMapping
    public ResponseEntity<ApiResponse<ModalResponseDTO>> criar(@RequestBody @Valid ModalRequestDTO dto) {
        return ApiResponse.created("Modal cadastrado com sucesso", modalService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ModalResponseDTO>>> listarTodos() {
        return ApiResponse.success("Modais listados com sucesso", modalService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ModalResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ApiResponse.success("Modal encontrado com sucesso", modalService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModalResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid ModalRequestDTO dto) {
        return ApiResponse.success("Modal atualizado com sucesso", modalService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        modalService.excluir(id);
        return ApiResponse.success("Modal excluído com sucesso", null);
    }
}

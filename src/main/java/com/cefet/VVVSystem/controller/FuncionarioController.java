package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.FuncionarioRequestDTO;
import com.cefet.VVVSystem.dto.FuncionarioResponseDTO;
import com.cefet.VVVSystem.response.ApiResponse;
import com.cefet.VVVSystem.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<ApiResponse<FuncionarioResponseDTO>> criar(@RequestBody @Valid FuncionarioRequestDTO dto) {
        return ApiResponse.created("Funcionário criado com sucesso", funcionarioService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FuncionarioResponseDTO>>> listarTodos() {
        return ApiResponse.success("Funcionários listados com sucesso", funcionarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncionarioResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ApiResponse.success("Funcionário encontrado com sucesso", funcionarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncionarioResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid FuncionarioRequestDTO dto) {
        return ApiResponse.success("Funcionário atualizado com sucesso", funcionarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        funcionarioService.excluir(id);
        return ApiResponse.success("Funcionário excluído com sucesso", null);
    }
}

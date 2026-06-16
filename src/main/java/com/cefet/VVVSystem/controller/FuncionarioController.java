package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.AlocarFuncionarioRequestDTO;
import com.cefet.VVVSystem.dto.FuncionarioRequestDTO;
import com.cefet.VVVSystem.dto.FuncionarioResponseDTO;
import com.cefet.VVVSystem.response.ApiResponse;
import com.cefet.VVVSystem.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@Tag(name = "Funcionários", description = "Endpoints para gerenciamento de funcionários")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_FUNCIONARIO_MANAGE)")
    public ResponseEntity<ApiResponse<FuncionarioResponseDTO>> criar(@RequestBody @Valid FuncionarioRequestDTO dto) {
        return ApiResponse.created("Funcionário criado com sucesso", funcionarioService.criar(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_FUNCIONARIO_MANAGE)")
    public ResponseEntity<ApiResponse<List<FuncionarioResponseDTO>>> listarTodos() {
        return ApiResponse.success("Funcionários listados com sucesso", funcionarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_FUNCIONARIO_MANAGE)")
    public ResponseEntity<ApiResponse<FuncionarioResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ApiResponse.success("Funcionário encontrado com sucesso", funcionarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_FUNCIONARIO_MANAGE)")
    public ResponseEntity<ApiResponse<FuncionarioResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid FuncionarioRequestDTO dto) {
        return ApiResponse.success("Funcionário atualizado com sucesso", funcionarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_FUNCIONARIO_MANAGE)")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        funcionarioService.excluir(id);
        return ApiResponse.success("Funcionário excluído com sucesso", null);
    }

    @Operation(summary = "Alocar funcionário em um Ponto de Venda", description = "Vincula um funcionário a um PDV, respeitando o limite máximo de 2 PDVs por funcionário.")
    @PostMapping("/{id}/pontos-de-venda")
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_FUNCIONARIO_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_FUNCIONARIO_ASSIGN)")
    public ResponseEntity<ApiResponse<FuncionarioResponseDTO>> alocarPontoDeVenda(
            @PathVariable Long id, 
            @RequestBody @Valid AlocarFuncionarioRequestDTO dto) {
        return ApiResponse.success("Funcionário alocado ao Ponto de Venda com sucesso", funcionarioService.alocarPontoDeVenda(id, dto.pdvId()));
    }
}

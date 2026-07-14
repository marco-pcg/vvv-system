package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.AtribuirGerenteRequestDTO;
import com.cefet.VVVSystem.dto.PontoDeVendaRequestDTO;
import com.cefet.VVVSystem.dto.PontoDeVendaResponseDTO;
import com.cefet.VVVSystem.response.ApiResponse;
import com.cefet.VVVSystem.service.PontoDeVendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/pontos-de-venda")
@Tag(name = "Pontos de Venda", description = "Endpoints para gerenciamento de Pontos de Venda")
public class PontoDeVendaController {

    @Autowired
    private PontoDeVendaService pontoDeVendaService;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_PDV_MANAGE)")
    public ResponseEntity<ApiResponse<PontoDeVendaResponseDTO>> criar(@RequestBody @Valid PontoDeVendaRequestDTO dto) {
        return ApiResponse.created("Ponto de Venda criado com sucesso", pontoDeVendaService.criar(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_PDV_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_PDV_READ)")
    public ResponseEntity<ApiResponse<List<PontoDeVendaResponseDTO>>> listarTodos() {
        return ApiResponse.success("Pontos de Venda listados com sucesso", pontoDeVendaService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_PDV_MANAGE, T(com.cefet.VVVSystem.security.RoleConstants).PERM_PDV_READ)")
    public ResponseEntity<ApiResponse<PontoDeVendaResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ApiResponse.success("Ponto de Venda encontrado com sucesso", pontoDeVendaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_PDV_MANAGE)")
    public ResponseEntity<ApiResponse<PontoDeVendaResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid PontoDeVendaRequestDTO dto) {
        return ApiResponse.success("Ponto de Venda atualizado com sucesso", pontoDeVendaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_PDV_MANAGE)")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        pontoDeVendaService.excluir(id);
        return ApiResponse.success("Ponto de Venda excluído com sucesso", null);
    }

    @Operation(summary = "Atribuir um Gerente a um PDV", description = "Define o funcionário que será o gerente responsável pelo Ponto de Venda.")
    @PatchMapping("/{id}/gerente")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_PDV_MANAGE)")
    public ResponseEntity<ApiResponse<PontoDeVendaResponseDTO>> atribuirGerente(
            @PathVariable Long id, 
            @RequestBody @Valid AtribuirGerenteRequestDTO dto) {
        return ApiResponse.success("Gerente atribuído com sucesso", pontoDeVendaService.atribuirGerente(id, dto.gerenteId()));
    }
}

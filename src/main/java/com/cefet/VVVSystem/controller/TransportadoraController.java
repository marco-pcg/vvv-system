package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.TransportadoraRequestDTO;
import com.cefet.VVVSystem.dto.TransportadoraResponseDTO;
import com.cefet.VVVSystem.response.ApiResponse;
import com.cefet.VVVSystem.service.TransportadoraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/transportadoras")
public class TransportadoraController {

    @Autowired
    private TransportadoraService transportadoraService;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_TRANSPORTADORA_MANAGE)")
    public ResponseEntity<ApiResponse<TransportadoraResponseDTO>> criar(@RequestBody @Valid TransportadoraRequestDTO dto) {
        return ApiResponse.created("Transportadora cadastrada com sucesso", transportadoraService.criar(dto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_TRANSPORTADORA_MANAGE)")
    public ResponseEntity<ApiResponse<List<TransportadoraResponseDTO>>> listarTodos() {
        return ApiResponse.success("Transportadoras listadas com sucesso", transportadoraService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_TRANSPORTADORA_MANAGE)")
    public ResponseEntity<ApiResponse<TransportadoraResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ApiResponse.success("Transportadora encontrada com sucesso", transportadoraService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_TRANSPORTADORA_MANAGE)")
    public ResponseEntity<ApiResponse<TransportadoraResponseDTO>> atualizar(@PathVariable Long id, @RequestBody @Valid TransportadoraRequestDTO dto) {
        return ApiResponse.success("Transportadora atualizada com sucesso", transportadoraService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_TRANSPORTADORA_MANAGE)")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        transportadoraService.excluir(id);
        return ApiResponse.success("Transportadora excluída com sucesso", null);
    }
}

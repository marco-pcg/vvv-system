package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.PagamentoFisicoRequestDTO;
import com.cefet.VVVSystem.response.ApiResponse;
import com.cefet.VVVSystem.service.PagamentoFisicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoFisicoController {

    private final PagamentoFisicoService pagamentoFisicoService;

    @PostMapping("/fisico")
    @PreAuthorize("hasAnyAuthority(T(com.cefet.VVVSystem.security.RoleConstants).PERM_PAGAMENTO_PROCESS, T(com.cefet.VVVSystem.security.RoleConstants).PERM_RESERVA_MANAGE_ALL)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> processarPagamento(
            @Valid @RequestBody PagamentoFisicoRequestDTO dto) {
        
        Map<String, Object> response = pagamentoFisicoService.processarPagamentoGuiche(dto);
        return ApiResponse.success("Pagamento efetuado e reserva confirmada.", response);
    }
}

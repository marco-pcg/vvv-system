package com.cefet.VVVSystem.controller;

import com.cefet.VVVSystem.dto.ClienteResponseDTO;
import com.cefet.VVVSystem.dto.ClienteUpdateDTO;
import com.cefet.VVVSystem.security.MainUser;
import com.cefet.VVVSystem.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponseDTO> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MainUser mainUser = (MainUser) auth.getPrincipal();
        return ResponseEntity.ok(clienteService.getMe(mainUser.getUser().getId()));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClienteResponseDTO> updateMe(@RequestBody ClienteUpdateDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MainUser mainUser = (MainUser) auth.getPrincipal();
        return ResponseEntity.ok(clienteService.updateMe(mainUser.getUser().getId(), dto));
    }
}

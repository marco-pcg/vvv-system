package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import com.cefet.VVVSystem.domain.entity.PontoDeVenda;
import com.cefet.VVVSystem.domain.repository.FuncionarioRepository;
import com.cefet.VVVSystem.domain.repository.PontoDeVendaRepository;
import com.cefet.VVVSystem.dto.FuncionarioResponseDTO;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private PontoDeVendaRepository pontoDeVendaRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    private Funcionario funcionario;
    private PontoDeVenda pdv1;
    private PontoDeVenda pdv2;
    private PontoDeVenda pdv3;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Funcionario Teste");

        pdv1 = new PontoDeVenda();
        pdv1.setId(10L);
        pdv1.setCnpj("00000000000001");

        pdv2 = new PontoDeVenda();
        pdv2.setId(20L);
        pdv2.setCnpj("00000000000002");

        pdv3 = new PontoDeVenda();
        pdv3.setId(30L);
        pdv3.setCnpj("00000000000003");
    }

    @Test
    @DisplayName("Deve alocar funcionário no primeiro PDV com sucesso")
    void alocarPrimeiroPdvComSucesso() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(pontoDeVendaRepository.findById(10L)).thenReturn(Optional.of(pdv1));
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(i -> i.getArgument(0));

        FuncionarioResponseDTO response = funcionarioService.alocarPontoDeVenda(1L, 10L);

        assertNotNull(response);
        assertEquals(1, funcionario.getPontosDeVenda().size());
        assertTrue(funcionario.getPontosDeVenda().contains(pdv1));
        
        verify(funcionarioRepository).save(funcionario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alocar no terceiro PDV (limite de 2)")
    void erroAoAlocarTerceiroPdv() {
        // Arrange
        funcionario.getPontosDeVenda().add(pdv1);
        funcionario.getPontosDeVenda().add(pdv2);

        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(pontoDeVendaRepository.findById(30L)).thenReturn(Optional.of(pdv3));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> 
            funcionarioService.alocarPontoDeVenda(1L, 30L)
        );

        assertTrue(exception.getMessage().contains("Limite excedido"));
        assertEquals(2, funcionario.getPontosDeVenda().size());
        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se funcionário não existir")
    void erroAlocarFuncionarioInexistente() {
        when(funcionarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            funcionarioService.alocarPontoDeVenda(99L, 10L)
        );

        verify(pontoDeVendaRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se PDV não existir")
    void erroAlocarPdvInexistente() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(pontoDeVendaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            funcionarioService.alocarPontoDeVenda(1L, 99L)
        );

        verify(funcionarioRepository, never()).save(any());
    }
}

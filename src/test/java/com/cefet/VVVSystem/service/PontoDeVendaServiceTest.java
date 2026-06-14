package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import com.cefet.VVVSystem.domain.entity.PontoDeVenda;
import com.cefet.VVVSystem.domain.repository.FuncionarioRepository;
import com.cefet.VVVSystem.domain.repository.PontoDeVendaRepository;
import com.cefet.VVVSystem.dto.PontoDeVendaRequestDTO;
import com.cefet.VVVSystem.dto.PontoDeVendaResponseDTO;
import com.cefet.VVVSystem.exception.ConflictException;
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
class PontoDeVendaServiceTest {

    @Mock
    private PontoDeVendaRepository pontoDeVendaRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private PontoDeVendaService pontoDeVendaService;

    private PontoDeVendaRequestDTO requestDTO;
    private Funcionario funcionario;
    private PontoDeVenda pontoDeVenda;

    @BeforeEach
    void setUp() {
        requestDTO = new PontoDeVendaRequestDTO("12345678000199", "Av. Maracanã, 229", 1L);

        funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome("Gerente Teste");

        pontoDeVenda = new PontoDeVenda();
        pontoDeVenda.setId(10L);
        pontoDeVenda.setCnpj("12345678000199");
        pontoDeVenda.setEndereco("Av. Maracanã, 229");
        pontoDeVenda.setGerente(funcionario);
    }

    @Test
    @DisplayName("Deve criar um Ponto de Venda com sucesso quando os dados forem válidos")
    void criarPontoDeVendaComSucesso() {
        // Arrange
        when(pontoDeVendaRepository.findByCnpj(requestDTO.cnpj())).thenReturn(Optional.empty());
        when(funcionarioRepository.findById(requestDTO.gerenteId())).thenReturn(Optional.of(funcionario));
        when(pontoDeVendaRepository.save(any(PontoDeVenda.class))).thenReturn(pontoDeVenda);

        // Act
        PontoDeVendaResponseDTO response = pontoDeVendaService.criar(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(pontoDeVenda.getId(), response.id());
        assertEquals(requestDTO.cnpj(), response.cnpj());
        assertEquals(requestDTO.gerenteId(), response.gerenteId());

        verify(pontoDeVendaRepository, times(1)).findByCnpj(requestDTO.cnpj());
        verify(funcionarioRepository, times(1)).findById(requestDTO.gerenteId());
        verify(pontoDeVendaRepository, times(1)).save(any(PontoDeVenda.class));
    }

    @Test
    @DisplayName("Deve lançar ConflictException ao tentar criar PDV com CNPJ já existente")
    void criarPontoDeVendaCnpjDuplicado() {
        // Arrange
        when(pontoDeVendaRepository.findByCnpj(requestDTO.cnpj())).thenReturn(Optional.of(pontoDeVenda));

        // Act & Assert
        assertThrows(ConflictException.class, () -> pontoDeVendaService.criar(requestDTO));

        verify(pontoDeVendaRepository, times(1)).findByCnpj(requestDTO.cnpj());
        verify(funcionarioRepository, never()).findById(any());
        verify(pontoDeVendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException se o gerente não existir no banco de dados")
    void criarPontoDeVendaGerenteNaoEncontrado() {
        // Arrange
        when(pontoDeVendaRepository.findByCnpj(requestDTO.cnpj())).thenReturn(Optional.empty());
        when(funcionarioRepository.findById(requestDTO.gerenteId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pontoDeVendaService.criar(requestDTO));

        verify(pontoDeVendaRepository, times(1)).findByCnpj(requestDTO.cnpj());
        verify(funcionarioRepository, times(1)).findById(requestDTO.gerenteId());
        verify(pontoDeVendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atribuir um novo gerente ao PDV com sucesso")
    void atribuirGerenteComSucesso() {
        // Arrange
        Funcionario novoGerente = new Funcionario();
        novoGerente.setId(2L);
        novoGerente.setNome("Novo Gerente");

        when(pontoDeVendaRepository.findById(pontoDeVenda.getId())).thenReturn(Optional.of(pontoDeVenda));
        when(funcionarioRepository.findById(novoGerente.getId())).thenReturn(Optional.of(novoGerente));
        when(pontoDeVendaRepository.save(any(PontoDeVenda.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        PontoDeVendaResponseDTO response = pontoDeVendaService.atribuirGerente(pontoDeVenda.getId(), novoGerente.getId());

        // Assert
        assertNotNull(response);
        assertEquals(novoGerente.getId(), response.gerenteId());

        verify(pontoDeVendaRepository, times(1)).findById(pontoDeVenda.getId());
        verify(funcionarioRepository, times(1)).findById(novoGerente.getId());
        verify(pontoDeVendaRepository, times(1)).save(any(PontoDeVenda.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar atribuir gerente a um PDV inexistente")
    void atribuirGerentePdvNaoEncontrado() {
        // Arrange
        when(pontoDeVendaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pontoDeVendaService.atribuirGerente(999L, 1L));

        verify(pontoDeVendaRepository, times(1)).findById(999L);
        verify(funcionarioRepository, never()).findById(any());
        verify(pontoDeVendaRepository, never()).save(any());
    }
}

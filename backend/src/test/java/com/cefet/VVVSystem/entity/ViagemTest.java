package com.cefet.VVVSystem.entity;

import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.entity.Viagem;
import com.cefet.VVVSystem.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViagemTest {

    private Viagem viagem;

    @BeforeEach
    void setUp() {
        viagem = new Viagem();
    }

    @Test
    void verificarDisponibilidadeCapacidade_Sucesso() {
        Modal modal1 = new Modal();
        modal1.setCapacidade(50);
        Modal modal2 = new Modal();
        modal2.setCapacidade(100);

        viagem.setModais(new HashSet<>(List.of(modal1, modal2)));

        // A capacidade mínima é 50. Com 40 reservas, não deve lançar exceção.
        assertDoesNotThrow(() -> viagem.verificarDisponibilidadeCapacidade(40L));
    }

    @Test
    void verificarDisponibilidadeCapacidade_Overbooking() {
        Modal modal1 = new Modal();
        modal1.setCapacidade(50);
        Modal modal2 = new Modal();
        modal2.setCapacidade(100);

        viagem.setModais(new HashSet<>(List.of(modal1, modal2)));

        // A capacidade mínima é 50. Com 50 reservas ou mais, deve lançar exceção.
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> viagem.verificarDisponibilidadeCapacidade(50L));
        
        assertTrue(exception.getMessage().contains("Não há mais assentos disponíveis"));
    }

    @Test
    void verificarDisponibilidadeCapacidade_SemModais() {
        viagem.setModais(new HashSet<>());

        BusinessException exception = assertThrows(BusinessException.class, 
            () -> viagem.verificarDisponibilidadeCapacidade(0L));
        
        assertEquals("A viagem não possui modais alocados.", exception.getMessage());
    }
}

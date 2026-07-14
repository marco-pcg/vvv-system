package com.cefet.VVVSystem.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cefet.VVVSystem.domain.entity.Funcionario;
import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.enums.StatusOperacional;
import com.cefet.VVVSystem.domain.enums.TipoModal;
import static org.junit.jupiter.api.Assertions.*;

public class ModalMaintenanceTest {

    @Test
    @DisplayName("Should change Modal status to non-operational when Funcionario puts it into maintenance") 
    void shouldPutModalInMaintenanceMode() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Marco");
        funcionario.setCpf("12345678909");

        Modal aviao = new Modal();
        aviao.setCodigo("BR-A320");
        aviao.setTipo(TipoModal.AVIAO);
        aviao.setCapacidade(180);
        aviao.setStatusOperacional(StatusOperacional.EM_MANUTENCAO);

        assertEquals(aviao.verificarStatusOperacional(), StatusOperacional.EM_MANUTENCAO);
    }

}

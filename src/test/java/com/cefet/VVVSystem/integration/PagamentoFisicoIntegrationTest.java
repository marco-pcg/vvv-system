package com.cefet.VVVSystem.integration;

import com.cefet.VVVSystem.domain.entity.*;
import com.cefet.VVVSystem.domain.enums.*;
import com.cefet.VVVSystem.domain.repository.*;
import com.cefet.VVVSystem.dto.PagamentoFisicoRequestDTO;
import com.cefet.VVVSystem.service.PagamentoFisicoService;
import com.cefet.VVVSystem.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PagamentoFisicoIntegrationTest {

    @Autowired
    private PagamentoFisicoService pagamentoFisicoService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PassageiroRepository passageiroRepository;

    @Autowired
    private ModalRepository modalRepository;

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    private Reserva reservaPendente;

    @BeforeEach
    public void setup() {
        pagamentoRepository.deleteAll();
        ticketRepository.deleteAll();
        reservaRepository.deleteAll();
        passageiroRepository.deleteAll();
        clienteRepository.deleteAll();
        viagemRepository.deleteAll();
        modalRepository.deleteAll();
        transportadoraRepository.deleteAll();
        cidadeRepository.deleteAll();

        // 1. Criar Transportadora e Cidade
        Cidade cidadeOrigem = new Cidade();
        cidadeOrigem.setNome("Rio de Janeiro");
        cidadeOrigem.setUf(UF.RJ);
        cidadeOrigem = cidadeRepository.save(cidadeOrigem);

        Cidade cidadeDestino = new Cidade();
        cidadeDestino.setNome("São Paulo");
        cidadeDestino.setUf(UF.SP);
        cidadeDestino = cidadeRepository.save(cidadeDestino);

        Transportadora transportadora = new Transportadora();
        transportadora.setNome("Viação Teste");
        transportadora.setCnpj("12345678000199");
        transportadora = transportadoraRepository.save(transportadora);

        // 2. Criar Modal
        Modal modal = new Modal();
        modal.setCodigo("MOD-TESTE");
        modal.setTipo(TipoModal.ONIBUS);
        modal.setCapacidade(40);
        modal.setStatusOperacional(StatusOperacional.OPERACIONAL);
        modal.setTransportadora(transportadora);
        modal = modalRepository.save(modal);

        // 3. Criar Viagem
        Viagem viagem = new Viagem();
        viagem.setCidadeOrigem(cidadeOrigem);
        viagem.setCidadeDestino(cidadeDestino);
        viagem.setPartida(LocalDateTime.now().plusDays(2));
        viagem.setChegada(LocalDateTime.now().plusDays(2).plusHours(6));
        viagem.setPreco(new BigDecimal("100.00"));
        viagem.setStatus(StatusViagem.AGENDADA);
        viagem = viagemRepository.save(viagem);

        // 4. Criar Cliente e Passageiro
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setCpf("11122233344");
        cliente.setEmail("cliente@teste.com");
        cliente.setTelefone("21999999999");
        cliente = clienteRepository.save(cliente);

        Passageiro passageiro = new Passageiro();
        passageiro.setNome("Passageiro Teste");
        passageiro.setCpf("55566677788");
        passageiro.setEmail("passageiro@teste.com");
        passageiro.setTelefone("21988888888");
        passageiro = passageiroRepository.save(passageiro);

        // 5. Criar Reserva PENDENTE
        Reserva reserva = new Reserva();
        reserva.setViagem(viagem);
        reserva.setCliente(cliente);
        reserva.setPassageiro(passageiro);
        reserva.setCodigo("RES-FIS-123");
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setValorTotal(new BigDecimal("100.00"));
        reserva.setDataCriacao(LocalDateTime.now());
        reserva.setIntegradoTransportadora(false);
        this.reservaPendente = reservaRepository.save(reserva);
    }

    @Test
    public void deveProcessarPagamentoEmDinheiroNoGuicheEEmitirTicket() {
        // Arrange
        PagamentoFisicoRequestDTO dto = new PagamentoFisicoRequestDTO(
                reservaPendente.getId(),
                TipoPagamento.DINHEIRO,
                null,
                null,
                150.0 // Valor recebido (troco será 50)
        );

        // Act
        Map<String, Object> response = pagamentoFisicoService.processarPagamentoGuiche(dto);

        // Assert
        assertTrue((Boolean) response.get("sucesso"));
        assertNotNull(response.get("ticketNumero"));
        assertEquals(StatusReserva.CONFIRMADA, response.get("reservaStatus"));

        // Validar Banco de Dados - Reserva
        Reserva reservaAtualizada = reservaRepository.findById(reservaPendente.getId()).get();
        assertEquals(StatusReserva.CONFIRMADA, reservaAtualizada.getStatus());

        // Validar Banco de Dados - Ticket
        Optional<Ticket> ticketOpt = ticketRepository.findByReservaId(reservaAtualizada.getId());
        assertTrue(ticketOpt.isPresent());
        assertEquals(ticketOpt.get().getNumero(), response.get("ticketNumero"));

        // Validar Banco de Dados - Pagamento
        assertEquals(1, pagamentoRepository.count());
    }

    @Test
    public void naoDevePermitirPagamentoFisicoParaReservaJaConfirmada() {
        // Arrange
        reservaPendente.confirmarPagamento();
        reservaRepository.save(reservaPendente);

        PagamentoFisicoRequestDTO dto = new PagamentoFisicoRequestDTO(
                reservaPendente.getId(),
                TipoPagamento.CREDITO,
                "1234123412341234",
                1,
                null
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pagamentoFisicoService.processarPagamentoGuiche(dto);
        });

        assertTrue(exception.getMessage().contains("Apenas reservas com status PENDENTE podem ser pagas no guichê"));
    }
}

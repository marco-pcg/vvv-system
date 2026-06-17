package com.cefet.VVVSystem.integration;

import com.cefet.VVVSystem.domain.entity.Cidade;
import com.cefet.VVVSystem.domain.entity.Cliente;
import com.cefet.VVVSystem.domain.entity.Modal;
import com.cefet.VVVSystem.domain.entity.Passageiro;
import com.cefet.VVVSystem.domain.entity.Transportadora;
import com.cefet.VVVSystem.domain.entity.Viagem;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import com.cefet.VVVSystem.domain.repository.CidadeRepository;
import com.cefet.VVVSystem.domain.repository.ClienteRepository;
import com.cefet.VVVSystem.domain.repository.ModalRepository;
import com.cefet.VVVSystem.domain.repository.PassageiroRepository;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import com.cefet.VVVSystem.domain.repository.TicketRepository;
import com.cefet.VVVSystem.domain.repository.TransportadoraRepository;
import com.cefet.VVVSystem.domain.repository.ViagemRepository;
import com.cefet.VVVSystem.dto.ReservaRequestDTO;
import com.cefet.VVVSystem.dto.VendaOnlineRequestDTO;
import com.cefet.VVVSystem.dto.VendaOnlineResponseDTO;
import com.cefet.VVVSystem.service.GerenciamentoVendasService;
import com.cefet.VVVSystem.service.VendaOnlineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FluxoVendaOnlineIntegrationTest {

    @Autowired
    private VendaOnlineService vendaOnlineService;

    @Autowired
    private GerenciamentoVendasService gerenciamentoVendasService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PassageiroRepository passageiroRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private TransportadoraRepository transportadoraRepository;

    @Autowired
    private ModalRepository modalRepository;

    @Test
    void fluxoCompleto_SolicitacaoEAprovacaoVenda() {
        // Preparar Dados (Mockando no banco em memória)
        Cidade cidadeOrigem = new Cidade();
        cidadeOrigem.setNome("Cidade A");
        cidadeOrigem.setUf(com.cefet.VVVSystem.domain.enums.UF.RJ);
        cidadeOrigem = cidadeRepository.save(cidadeOrigem);

        Cidade cidadeDestino = new Cidade();
        cidadeDestino.setNome("Cidade B");
        cidadeDestino.setUf(com.cefet.VVVSystem.domain.enums.UF.SP);
        cidadeDestino = cidadeRepository.save(cidadeDestino);

        Transportadora transportadora = new Transportadora();
        transportadora.setNome("Transportadora A");
        transportadora.setCnpj("12345678000199");
        transportadora = transportadoraRepository.save(transportadora);

        Modal modal = new Modal();
        modal.setCodigo("MOD-TESTE");
        modal.setTransportadora(transportadora);
        modal.setTipo(com.cefet.VVVSystem.domain.enums.TipoModal.ONIBUS);
        modal.setStatusOperacional(com.cefet.VVVSystem.domain.enums.StatusOperacional.OPERACIONAL);
        modal.setCapacidade(40);
        modal = modalRepository.save(modal);

        Viagem viagem = new Viagem();
        viagem.setPreco(new BigDecimal("100.00"));
        viagem.setPartida(LocalDateTime.now().plusDays(1));
        viagem.setChegada(LocalDateTime.now().plusDays(1).plusHours(4));
        viagem.setCidadeOrigem(cidadeOrigem);
        viagem.setCidadeDestino(cidadeDestino);
        viagem.setStatus(com.cefet.VVVSystem.domain.enums.StatusViagem.AGENDADA);
        viagem.setModais(java.util.Set.of(modal));
        viagem = viagemRepository.save(viagem);

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setCpf("12345678901");
        cliente.setEmail("cliente@teste.com");
        cliente = clienteRepository.save(cliente);

        Passageiro passageiro = new Passageiro();
        passageiro.setNome("Passageiro Teste");
        passageiro.setCpf("10987654321");
        passageiro.setEmail("passageiro@teste.com");
        passageiro = passageiroRepository.save(passageiro);

        // 1. Cliente solicita a venda
        ReservaRequestDTO reservaDTO = new ReservaRequestDTO();
        reservaDTO.setIdViagem(viagem.getId());
        reservaDTO.setIdCliente(cliente.getId());
        reservaDTO.setIdPassageiro(passageiro.getId());

        VendaOnlineRequestDTO requestDTO = new VendaOnlineRequestDTO();
        requestDTO.setReserva(reservaDTO);
        requestDTO.setTipoPagamento(TipoPagamento.CREDITO);
        requestDTO.setNumeroCartao("1234123412341234");
        requestDTO.setParcelas(3);

        VendaOnlineResponseDTO response = vendaOnlineService.solicitarVenda(requestDTO);
        assertNotNull(response);
        Long reservaId = response.getReservaId();

        // 2. Verificar se a reserva está AGUARDANDO_APROVACAO no banco
        var reserva = reservaRepository.findById(reservaId).orElseThrow();
        assertEquals(StatusReserva.AGUARDANDO_APROVACAO, reserva.getStatus());
        assertFalse(reserva.getIntegradoTransportadora());

        // 3. Gerente aprova a venda online
        var ticketEmitido = gerenciamentoVendasService.aprovarVendaOnline(reservaId);
        assertNotNull(ticketEmitido);

        // 4. Verificar se a reserva está CONFIRMADA
        var reservaAprovada = reservaRepository.findById(reservaId).orElseThrow();
        assertEquals(StatusReserva.CONFIRMADA, reservaAprovada.getStatus());

        // 5. Verificar se o ticket foi emitido
        var ticket = ticketRepository.findByReservaId(reservaId);
        assertTrue(ticket.isPresent());
        assertNotNull(ticket.get().getNumero());
    }
}

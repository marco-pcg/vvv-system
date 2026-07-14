package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.*;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import com.cefet.VVVSystem.domain.repository.PagamentoRepository;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import com.cefet.VVVSystem.dto.PagamentoFisicoRequestDTO;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PagamentoFisicoService {

    private final ReservaRepository reservaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ProcessadorPagamento processadorPagamento;
    private final TicketService ticketService;

    @Transactional
    public Map<String, Object> processarPagamentoGuiche(PagamentoFisicoRequestDTO dto) {
        // 1. Validar a Reserva
        Reserva reserva = reservaRepository.findById(dto.idReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", dto.idReserva()));

        if (reserva.getStatus() != StatusReserva.PENDENTE) {
            throw new BusinessException("Apenas reservas com status PENDENTE podem ser pagas no guichê. Status atual: " + reserva.getStatus());
        }

        // 2. Instanciar o Pagamento
        Pagamento pagamento = instanciarPagamento(dto);
        pagamento.setTipo(dto.tipoPagamento());

        // 3. Processar Pagamento (Strategy) e mudar status para CONFIRMADA
        processadorPagamento.processarERegistrarPagamento(reserva, pagamento, StatusReserva.CONFIRMADA);

        // 4. Salvar Entidades
        pagamentoRepository.save(pagamento);
        reservaRepository.save(reserva);

        // 5. Emitir Ticket automaticamente após o pagamento confirmado
        Ticket ticket = ticketService.emitirTicket(reserva);

        return Map.of(
            "sucesso", true,
            "mensagem", "Pagamento aprovado. Ticket emitido com sucesso no Guichê.",
            "ticketNumero", ticket.getNumero(),
            "assento", ticket.getAssento(),
            "reservaStatus", reserva.getStatus()
        );
    }

    private Pagamento instanciarPagamento(PagamentoFisicoRequestDTO dto) {
        if (dto.tipoPagamento() == TipoPagamento.CREDITO) {
            if (dto.numeroCartao() == null || dto.parcelas() == null) {
                throw new BusinessException("Para pagamento em crédito, numeroCartao e parcelas são obrigatórios.");
            }
            PagamentoCredito credito = new PagamentoCredito();
            credito.setNumeroCartao(dto.numeroCartao());
            credito.setParcelas(dto.parcelas());
            return credito;
        } else if (dto.tipoPagamento() == TipoPagamento.DEBITO) {
            if (dto.numeroCartao() == null) {
                throw new BusinessException("Para pagamento em débito, o numeroCartao é obrigatório.");
            }
            PagamentoDebito debito = new PagamentoDebito();
            debito.setNumeroCartao(dto.numeroCartao());
            return debito;
        } else if (dto.tipoPagamento() == TipoPagamento.DINHEIRO) {
            if (dto.valorRecebido() == null) {
                throw new BusinessException("Para pagamento em dinheiro, o valorRecebido é obrigatório.");
            }
            PagamentoDinheiro dinheiro = new PagamentoDinheiro();
            dinheiro.setValorRecebido(dto.valorRecebido());
            // O troco será calculado dentro do PagamentoDinheiroStrategy
            dinheiro.setTroco(0.0); 
            return dinheiro;
        } else {
            throw new BusinessException("Tipo de pagamento não suportado.");
        }
    }
}

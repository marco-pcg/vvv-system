package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Pagamento;
import com.cefet.VVVSystem.domain.entity.PagamentoCredito;
import com.cefet.VVVSystem.domain.entity.PagamentoDebito;
import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.enums.TipoPagamento;
import com.cefet.VVVSystem.domain.repository.PagamentoRepository;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import com.cefet.VVVSystem.dto.ReservaResponseDTO;
import com.cefet.VVVSystem.dto.VendaOnlineRequestDTO;
import com.cefet.VVVSystem.dto.VendaOnlineResponseDTO;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendaOnlineService {

    private final ReservaService reservaService;
    private final ReservaRepository reservaRepository;
    private final ProcessadorPagamento processadorPagamento;
    private final PagamentoRepository pagamentoRepository;

    @Transactional
    public VendaOnlineResponseDTO solicitarVenda(VendaOnlineRequestDTO dto) {
        // 1. Criar a reserva inicialmente (fica com status PENDENTE internamente)
        ReservaResponseDTO reservaResponse = reservaService.create(dto.getReserva());
        
        Reserva reserva = reservaRepository.findById(reservaResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", reservaResponse.getId()));

        // 2. Criar a entidade de pagamento adequada com base no tipo
        Pagamento pagamento = instanciarPagamento(dto);
        pagamento.setTipo(dto.getTipoPagamento());

        // 3. Processar o pagamento e atualizar status para AGUARDANDO_APROVACAO
        processadorPagamento.processarERegistrarPagamento(reserva, pagamento, StatusReserva.AGUARDANDO_APROVACAO);

        // 4. Salvar as entidades no banco
        pagamento = pagamentoRepository.save(pagamento);
        reserva = reservaRepository.save(reserva);

        return new VendaOnlineResponseDTO(
                reserva.getId(),
                reserva.getCodigo(),
                reserva.getStatus(),
                "Venda online solicitada. Aguardando aprovação gerencial."
        );
    }

    private Pagamento instanciarPagamento(VendaOnlineRequestDTO dto) {
        if (dto.getTipoPagamento() == TipoPagamento.CREDITO) {
            PagamentoCredito credito = new PagamentoCredito();
            credito.setNumeroCartao(dto.getNumeroCartao());
            credito.setParcelas(dto.getParcelas());
            return credito;
        } else if (dto.getTipoPagamento() == TipoPagamento.DEBITO) {
            PagamentoDebito debito = new PagamentoDebito();
            debito.setNumeroCartao(dto.getNumeroCartao());
            return debito;
        } else {
            throw new BusinessException("Apenas pagamentos por CRÉDITO ou DÉBITO são aceitos em vendas online.");
        }
    }
}

package com.cefet.VVVSystem.service;

import com.cefet.VVVSystem.domain.entity.Cliente;
import com.cefet.VVVSystem.domain.entity.Passageiro;
import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.entity.Viagem;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.repository.ClienteRepository;
import com.cefet.VVVSystem.domain.repository.PassageiroRepository;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import com.cefet.VVVSystem.domain.repository.ViagemRepository;
import com.cefet.VVVSystem.dto.ReservaRequestDTO;
import com.cefet.VVVSystem.dto.ReservaResponseDTO;
import com.cefet.VVVSystem.exception.BusinessException;
import com.cefet.VVVSystem.exception.ResourceNotFoundException;
import com.cefet.VVVSystem.mapper.ReservaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ViagemRepository viagemRepository;
    private final ClienteRepository clienteRepository;
    private final PassageiroRepository passageiroRepository;
    private final DescontoService descontoService;
    private final ReservaMapper reservaMapper;

    /**
     * Status considerados "ativos" para fins de controle de capacidade.
     */
    private static final List<StatusReserva> STATUSES_ATIVOS = List.of(
            StatusReserva.PENDENTE,
            StatusReserva.CONFIRMADA,
            StatusReserva.AGUARDANDO_APROVACAO
    );

    @Transactional
    public ReservaResponseDTO create(ReservaRequestDTO dto) {
        // 1. Validar Viagem
        Viagem viagem = viagemRepository.findById(dto.getIdViagem())
                .orElseThrow(() -> new ResourceNotFoundException("Viagem", "id", dto.getIdViagem()));

        // 2. Validar Cliente
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", dto.getIdCliente()));

        // 3. Validar Passageiro
        Passageiro passageiro = passageiroRepository.findById(dto.getIdPassageiro())
                .orElseThrow(() -> new ResourceNotFoundException("Passageiro", "id", dto.getIdPassageiro()));

        // 4. Controle de capacidade / Anti-overbooking (RF09/RI04)
        long reservasAtivas = reservaRepository.countByViagemIdAndStatusIn(viagem.getId(), STATUSES_ATIVOS);
        viagem.verificarDisponibilidadeCapacidade(reservasAtivas);

        // 5. Cálculo de desconto (RF15/RI03) — valor final calculado automaticamente
        BigDecimal precoOriginal = viagem.getPreco();
        BigDecimal valorFinal = descontoService.calcularPrecoComDesconto(
                precoOriginal, passageiro, viagem.getPartida().toLocalDate());

        // 6. Salvar reserva
        Reserva reserva = new Reserva();
        reserva.setCodigo(gerarCodigoReserva());
        reserva.setViagem(viagem);
        reserva.setCliente(cliente);
        reserva.setPassageiro(passageiro);
        reserva.setDataCriacao(LocalDateTime.now());
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setValorTotal(valorFinal);

        reserva = reservaRepository.save(reserva);
        return reservaMapper.toResponseDTO(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> findAll() {
        return reservaRepository.findAll().stream()
                .map(reservaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservaResponseDTO findById(Long id) {
        Reserva reserva = getReservaById(id);
        return reservaMapper.toResponseDTO(reserva);
    }

    @Transactional
    public void cancel(Long id) {
        Reserva reserva = getReservaById(id);
        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new BusinessException("Esta reserva já está cancelada.");
        }
        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    private Reserva getReservaById(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));
    }

    private String gerarCodigoReserva() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

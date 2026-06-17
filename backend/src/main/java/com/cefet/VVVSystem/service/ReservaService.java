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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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
    private final ReservaMapper reservaMapper;

    private static final BigDecimal DESCONTO_RN14_CRIANCA = new BigDecimal("0.40");   // 40%
    private static final BigDecimal SEM_DESCONTO = BigDecimal.ZERO;

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

        // 3.1. Anti-Duplicidade de Reserva (RN11: Um passageiro não pode ter duas reservas na MESMA viagem)
        boolean jaPossuiReserva = reservaRepository.existsByPassageiroIdAndViagemIdAndStatusIn(
                passageiro.getId(), viagem.getId(), STATUSES_ATIVOS);
        if (jaPossuiReserva) {
            throw new BusinessException("O passageiro já possui uma reserva ativa para esta viagem.");
        }

        // 4. Controle de capacidade / Anti-overbooking (RF09/RI04)
        long reservasAtivas = reservaRepository.countByViagemIdAndStatusIn(viagem.getId(), STATUSES_ATIVOS);
        viagem.verificarDisponibilidadeCapacidade(reservasAtivas);

        // 5. Cálculo de desconto (RF15/RI03) — valor final calculado internamente
        BigDecimal precoOriginal = viagem.getPreco();
        BigDecimal valorFinal = this.calcularPrecoComDesconto(
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
    public List<ReservaResponseDTO> findMinhasReservas(Long usuarioId) {
        return reservaRepository.findByClienteUserId(usuarioId).stream()
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

    private BigDecimal calcularPrecoComDesconto(BigDecimal precoOriginal, Passageiro passageiro, LocalDate dataViagem) {
        BigDecimal percentualDesconto = calcularPercentualDesconto(passageiro, dataViagem);
        BigDecimal valorDesconto = precoOriginal.multiply(percentualDesconto);
        return precoOriginal.subtract(valorDesconto).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private BigDecimal calcularPercentualDesconto(Passageiro passageiro, LocalDate dataReferencia) {
        if (passageiro.getDataNascimento() == null) {
            return SEM_DESCONTO;
        }

        int idade = Period.between(passageiro.getDataNascimento(), dataReferencia).getYears();

        // RN14: Crianças entre 2 e 10 anos têm 40% de desconto se acompanhadas
        if (idade >= 2 && idade <= 10 && Boolean.TRUE.equals(passageiro.getPossuiAcompanhante())) {
            return DESCONTO_RN14_CRIANCA;
        }

        return SEM_DESCONTO;
    }
}

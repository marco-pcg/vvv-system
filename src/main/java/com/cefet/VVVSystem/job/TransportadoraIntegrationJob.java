package com.cefet.VVVSystem.job;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import com.cefet.VVVSystem.domain.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransportadoraIntegrationJob {

    private final ReservaRepository reservaRepository;

    // Roda a cada 30 segundos para efeito de demonstração (em prod seria, por exemplo, a cada hora)
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void integrarReservasComTransportadoras() {
        log.info("Iniciando Job de Integração com as Transportadoras...");

        List<Reserva> reservasNaoIntegradas = reservaRepository.findByStatusAndIntegradoTransportadoraFalse(StatusReserva.CONFIRMADA);

        if (reservasNaoIntegradas.isEmpty()) {
            log.info("Nenhuma reserva pendente de integração.");
            return;
        }

        for (Reserva reserva : reservasNaoIntegradas) {
            try {
                // Simulação do envio de dados para a API/Fila da Transportadora
                log.info("Enviando dados da Reserva {} (Viagem: {}) para a Transportadora...", 
                        reserva.getCodigo(), reserva.getViagem().getId());
                
                // Em um cenário real, aqui entraria um RestTemplate, FeignClient ou RabbitMQ/Kafka producer.
                Thread.sleep(100); // Simulando I/O

                // Marca como integrado
                reserva.setIntegradoTransportadora(true);
                log.info("Reserva {} integrada com sucesso.", reserva.getCodigo());

            } catch (Exception e) {
                log.error("Erro ao integrar a reserva {}: {}", reserva.getCodigo(), e.getMessage());
            }
        }

        reservaRepository.saveAll(reservasNaoIntegradas);
        log.info("Job de Integração finalizado. {} reservas processadas.", reservasNaoIntegradas.size());
    }
}

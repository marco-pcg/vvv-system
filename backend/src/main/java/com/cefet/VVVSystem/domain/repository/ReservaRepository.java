package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Reserva;
import com.cefet.VVVSystem.domain.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Conta o número de reservas ativas (não canceladas e não expiradas) para uma viagem.
     * Usado para controle de capacidade / anti-overbooking.
     */
    long countByViagemIdAndStatusIn(Long viagemId, List<StatusReserva> statuses);

    List<Reserva> findByStatusAndIntegradoTransportadoraFalse(StatusReserva status);

    /**
     * Verifica se já existe uma reserva ativa para um determinado passageiro em uma viagem.
     */
    boolean existsByPassageiroIdAndViagemIdAndStatusIn(Long passageiroId, Long viagemId, List<StatusReserva> statuses);
}

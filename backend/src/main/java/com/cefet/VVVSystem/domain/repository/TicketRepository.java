package com.cefet.VVVSystem.domain.repository;

import com.cefet.VVVSystem.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByReservaId(Long reservaId);
    Optional<Ticket> findByNumero(String numero);
}

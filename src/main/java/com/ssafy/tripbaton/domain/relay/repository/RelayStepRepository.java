package com.ssafy.tripbaton.domain.relay.repository;

import com.ssafy.tripbaton.domain.relay.entity.RelayStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelayStepRepository extends JpaRepository<RelayStep, Long> {

    List<RelayStep> findByRelayIdOrderByStepOrderAsc(Long relayId);

    int countByRelayId(Long relayId);

    @org.springframework.data.jpa.repository.Query(
        "SELECT DISTINCT s.relay FROM RelayStep s JOIN FETCH s.relay.category " +
        "WHERE s.user.id = :userId ORDER BY s.relay.lastParticipatedAt DESC")
    java.util.List<com.ssafy.tripbaton.domain.relay.entity.Relay> findDistinctRelaysByUserId(
        @org.springframework.data.repository.query.Param("userId") Long userId);
}

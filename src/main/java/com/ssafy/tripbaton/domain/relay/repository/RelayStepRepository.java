package com.ssafy.tripbaton.domain.relay.repository;

import com.ssafy.tripbaton.domain.relay.entity.RelayStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelayStepRepository extends JpaRepository<RelayStep, Long> {

    List<RelayStep> findByRelayIdOrderByStepOrderAsc(Long relayId);
}

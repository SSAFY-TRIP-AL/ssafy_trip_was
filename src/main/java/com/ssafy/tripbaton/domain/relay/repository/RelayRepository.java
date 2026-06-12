package com.ssafy.tripbaton.domain.relay.repository;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelayRepository extends JpaRepository<Relay, Long> {
}

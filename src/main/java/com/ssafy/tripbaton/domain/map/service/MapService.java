package com.ssafy.tripbaton.domain.map.service;

import com.ssafy.tripbaton.domain.map.dto.MarkerItemDto;
import com.ssafy.tripbaton.domain.map.dto.MarkerListResponseDto;
import com.ssafy.tripbaton.domain.relay.entity.Relay;
import com.ssafy.tripbaton.domain.relay.entity.RelayStep;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.relay.repository.RelayStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MapService {

    private final RelayRepository relayRepository;
    private final RelayStepRepository relayStepRepository;

    @Transactional(readOnly = true)
    public MarkerListResponseDto getMarkers(Long categoryId) {
        List<Relay> relays = relayRepository.findActiveForMap(categoryId);

        List<MarkerItemDto> markers = relays.stream()
                .map(relay -> {
                    Optional<RelayStep> lastStep = relayStepRepository
                            .findTopByRelayIdOrderByStepOrderDesc(relay.getId());
                    return lastStep.map(step -> new MarkerItemDto(relay, step)).orElse(null);
                })
                .filter(item -> item != null)
                .toList();

        return new MarkerListResponseDto(markers);
    }
}

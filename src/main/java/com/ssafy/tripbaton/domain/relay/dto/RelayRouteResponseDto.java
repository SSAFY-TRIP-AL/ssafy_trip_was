package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import lombok.Getter;

import java.util.List;

@Getter
public class RelayRouteResponseDto {

    private final Long relayId;
    private final String title;
    private final List<RelayRouteStepDto> steps;

    public RelayRouteResponseDto(Relay relay, List<RelayRouteStepDto> steps) {
        this.relayId = relay.getId();
        this.title = relay.getTitle();
        this.steps = steps;
    }
}

package com.ssafy.tripbaton.domain.map.dto;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import com.ssafy.tripbaton.domain.relay.entity.RelayStep;
import lombok.Getter;

@Getter
public class MarkerItemDto {

    private final Long relayId;
    private final String title;
    private final String category;
    private final String status;
    private final LastLocationDto lastLocation;

    public MarkerItemDto(Relay relay, RelayStep lastStep) {
        this.relayId = relay.getId();
        this.title = relay.getTitle();
        this.category = relay.getCategory().getName();
        this.status = relay.getStatus().name();
        this.lastLocation = new LastLocationDto(lastStep);
    }
}

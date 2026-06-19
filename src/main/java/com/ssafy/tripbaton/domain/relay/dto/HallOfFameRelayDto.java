package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import lombok.Getter;

@Getter
public class HallOfFameRelayDto {

    private final Long id;
    private final String title;
    private final String category;
    private final int participantCount;

    public HallOfFameRelayDto(Relay relay, int participantCount) {
        this.id = relay.getId();
        this.title = relay.getTitle();
        this.category = relay.getCategory().getName();
        this.participantCount = participantCount;
    }
}

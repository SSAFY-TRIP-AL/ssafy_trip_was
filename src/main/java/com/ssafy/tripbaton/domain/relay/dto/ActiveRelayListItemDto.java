package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActiveRelayListItemDto {

    private final Long id;
    private final String title;
    private final String category;
    private final int participantCount;
    private final LocalDateTime lastParticipatedAt;

    public ActiveRelayListItemDto(Relay relay) {
        this.id = relay.getId();
        this.title = relay.getTitle();
        this.category = relay.getCategory().getName();
        this.participantCount = relay.getParticipantCount();
        this.lastParticipatedAt = relay.getLastParticipatedAt();
    }
}

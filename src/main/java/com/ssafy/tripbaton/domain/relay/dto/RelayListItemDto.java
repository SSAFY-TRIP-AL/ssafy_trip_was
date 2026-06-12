package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RelayListItemDto {

    private final Long id;
    private final String title;
    private final String category;
    private final int participantCount;
    private final String status;
    private final LocalDateTime lastParticipatedAt;
    private final LocalDateTime createdAt;

    public RelayListItemDto(Relay relay) {
        this.id = relay.getId();
        this.title = relay.getTitle();
        this.category = relay.getCategory().getName();
        this.participantCount = relay.getParticipantCount();
        this.status = relay.getStatus().name();
        this.lastParticipatedAt = relay.getLastParticipatedAt();
        this.createdAt = relay.getCreatedAt();
    }
}

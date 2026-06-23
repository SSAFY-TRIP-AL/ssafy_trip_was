package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RelayDetailResponseDto {

    private final Long id;
    private final String title;
    private final String category;
    private final int participantCount;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastParticipatedAt;
    private final List<RelayStepDto> steps;
    private final boolean isBookmarked;

    public RelayDetailResponseDto(Relay relay, List<RelayStepDto> steps, boolean isBookmarked) {
        this.id = relay.getId();
        this.title = relay.getTitle();
        this.category = relay.getCategory().getName();
        this.participantCount = relay.getParticipantCount();
        this.status = relay.getStatus().name();
        this.createdAt = relay.getCreatedAt();
        this.lastParticipatedAt = relay.getLastParticipatedAt();
        this.steps = steps;
        this.isBookmarked = isBookmarked;
    }
}

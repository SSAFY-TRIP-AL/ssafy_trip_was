package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CreatedRelayListItemDto {

    private final Long id;
    private final String title;
    private final String category;
    private final int participantCount;
    private final String status;
    private final String createdAt;
    private final String photoUrl;
    private final String content;

    public CreatedRelayListItemDto(Relay relay) {
        this.id = relay.getId();
        this.title = relay.getTitle();
        this.category = relay.getCategory().getName();
        this.participantCount = relay.getParticipantCount();
        this.status = relay.getStatus().name();
        this.createdAt = relay.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.photoUrl = relay.getPhotoUrl();
        this.content = relay.getContent();
    }
}

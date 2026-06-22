package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyRelayListItemDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String photoUrl;
    private final String status;
    private final LocalDateTime date;

    public MyRelayListItemDto(Relay relay) {
        this.id = relay.getId();
        this.title = relay.getTitle();
        this.content = relay.getContent();
        this.photoUrl = relay.getPhotoUrl();
        this.status = relay.getStatus().name();
        this.date = relay.getLastParticipatedAt();
    }
}

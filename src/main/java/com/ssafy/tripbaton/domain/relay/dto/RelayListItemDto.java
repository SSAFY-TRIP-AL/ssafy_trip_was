package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.bookmark.repository.BookmarkRepository;
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
    private final boolean isBookmarked;
    private final String photoUrl;
    private final Double latitude;
    private final Double longitude;

    public RelayListItemDto(Relay relay, boolean isBookmarked) {
        this.id = relay.getId();
        this.title = relay.getTitle();
        this.category = relay.getCategory().getName();
        this.participantCount = relay.getParticipantCount();
        this.status = relay.getStatus().name();
        this.lastParticipatedAt = relay.getLastParticipatedAt();
        this.createdAt = relay.getCreatedAt();
        this.isBookmarked = isBookmarked;
        this.photoUrl = relay.getPhotoUrl();
        this.latitude = relay.getLatitude();
        this.longitude=relay.getLongitude();
    }
}

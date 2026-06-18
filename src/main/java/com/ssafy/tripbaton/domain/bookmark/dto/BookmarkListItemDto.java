package com.ssafy.tripbaton.domain.bookmark.dto;

import com.ssafy.tripbaton.domain.bookmark.entity.Bookmark;
import lombok.Getter;

@Getter
public class BookmarkListItemDto {

    private final Long relayId;
    private final String title;
    private final String category;
    private final int participantCount;
    private final String thumbnailUrl;

    public BookmarkListItemDto(Bookmark bookmark, String thumbnailUrl) {
        this.relayId = bookmark.getRelay().getId();
        this.title = bookmark.getRelay().getTitle();
        this.category = bookmark.getRelay().getCategory().getName();
        this.participantCount = bookmark.getRelay().getParticipantCount();
        this.thumbnailUrl = thumbnailUrl;
    }
}

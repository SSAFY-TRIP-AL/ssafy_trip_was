package com.ssafy.tripbaton.domain.bookmark.dto;

import com.ssafy.tripbaton.domain.bookmark.entity.Bookmark;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookmarkListItemDto {

    private final Long id;
    private final String title;
    private final String category;
    private final int participantCount;
    private final String photoUrl;
    private final String content;

    public BookmarkListItemDto(Bookmark bookmark, String photoUrl) {
        this.id = bookmark.getRelay().getId();
        this.title = bookmark.getRelay().getTitle();
        this.category = bookmark.getRelay().getCategory().getName();
        this.participantCount = bookmark.getRelay().getParticipantCount();
        this.photoUrl = photoUrl;
        this.content = bookmark.getRelay().getContent();
    }
}

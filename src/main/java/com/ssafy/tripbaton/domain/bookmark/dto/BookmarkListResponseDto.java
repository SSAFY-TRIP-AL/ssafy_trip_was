package com.ssafy.tripbaton.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class BookmarkListResponseDto {

    private final List<BookmarkListItemDto> bookmarks;

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public BookmarkListResponseDto(Page<BookmarkListItemDto> page) {
        this.bookmarks = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}

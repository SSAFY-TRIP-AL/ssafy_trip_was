package com.ssafy.tripbaton.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookmarkListResponseDto {

    private List<BookmarkListItemDto> bookmarks;
}

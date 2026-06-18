package com.ssafy.tripbaton.domain.bookmark.controller;

import com.ssafy.tripbaton.domain.bookmark.service.BookmarkService;
import com.ssafy.tripbaton.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "bookmark")
@RestController
@RequestMapping("/api/relays")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{relayId}/bookmarks")
    public ResponseEntity<ApiResponse> addBookmark(Authentication authentication,
                                                   @PathVariable Long relayId) {
        Long userId = (Long) authentication.getPrincipal();
        bookmarkService.addBookmark(userId, relayId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("찜하기가 완료되었습니다."));
    }
}

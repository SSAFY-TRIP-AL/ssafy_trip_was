package com.ssafy.tripbaton.domain.bookmark.service;

import com.ssafy.tripbaton.domain.bookmark.dto.BookmarkListItemDto;
import com.ssafy.tripbaton.domain.bookmark.dto.BookmarkListResponseDto;
import com.ssafy.tripbaton.domain.bookmark.entity.Bookmark;
import com.ssafy.tripbaton.domain.bookmark.repository.BookmarkRepository;
import com.ssafy.tripbaton.domain.relay.entity.Relay;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.relay.repository.RelayStepRepository;
import com.ssafy.tripbaton.domain.user.entity.User;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;

import java.util.List;

import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RelayRepository relayRepository;
    private final RelayStepRepository relayStepRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public BookmarkListResponseDto getMyBookmarks(Long userId, Pageable pageable) {

        Page<Bookmark> page = bookmarkRepository.findByUserId(userId, pageable);

        Page<BookmarkListItemDto> dtoPage = page.map(bookmark -> {
            String thumbnailUrl = relayStepRepository
                    .findTopByRelayIdOrderByStepOrderAsc(bookmark.getRelay().getId())
                    .map(step -> step.getPhotoUrl())
                    .orElse(null);

            return new BookmarkListItemDto(bookmark, thumbnailUrl);
        });

        return new BookmarkListResponseDto(dtoPage);
    }

    @Transactional
    public void addBookmark(Long userId, Long relayId) {
        Relay relay = relayRepository.findById(relayId)
                .orElseThrow(() -> new CustomException(ErrorCode.RELAY_NOT_FOUND));

        if (bookmarkRepository.existsByUserIdAndRelayId(userId, relayId)) {
            throw new CustomException(ErrorCode.ALREADY_BOOKMARKED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        // Relay 엔티티에 true 설정
        relay.setBookmarked(true);
        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .relay(relay)
                .build());
    }

    @Transactional
    public void removeBookmark(Long userId, Long relayId) {
        Relay relay = relayRepository.findById(relayId)
                .orElseThrow(() -> new CustomException(ErrorCode.RELAY_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUserIdAndRelayId(userId, relayId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        // Relay 엔티티에 false 설정
        relay.setBookmarked(false);
        bookmarkRepository.delete(bookmark);
    }
}

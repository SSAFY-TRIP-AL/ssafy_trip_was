package com.ssafy.tripbaton.domain.bookmark.service;

import com.ssafy.tripbaton.domain.bookmark.entity.Bookmark;
import com.ssafy.tripbaton.domain.bookmark.repository.BookmarkRepository;
import com.ssafy.tripbaton.domain.relay.entity.Relay;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.user.entity.User;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RelayRepository relayRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addBookmark(Long userId, Long relayId) {
        Relay relay = relayRepository.findById(relayId)
                .orElseThrow(() -> new CustomException(ErrorCode.RELAY_NOT_FOUND));

        if (bookmarkRepository.existsByUserIdAndRelayId(userId, relayId)) {
            throw new CustomException(ErrorCode.ALREADY_BOOKMARKED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .relay(relay)
                .build());
    }
}

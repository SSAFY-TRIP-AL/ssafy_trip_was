package com.ssafy.tripbaton.domain.bookmark.repository;

import com.ssafy.tripbaton.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndRelayId(Long userId, Long relayId);
}

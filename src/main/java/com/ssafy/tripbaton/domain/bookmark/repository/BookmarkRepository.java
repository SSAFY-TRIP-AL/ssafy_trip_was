package com.ssafy.tripbaton.domain.bookmark.repository;

import com.ssafy.tripbaton.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndRelayId(Long userId, Long relayId);

    java.util.Optional<Bookmark> findByUserIdAndRelayId(Long userId, Long relayId);

    @org.springframework.data.jpa.repository.Query(
        "SELECT b FROM Bookmark b JOIN FETCH b.relay r JOIN FETCH r.category " +
        "WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    java.util.List<Bookmark> findAllByUserIdWithRelay(
        @org.springframework.data.repository.query.Param("userId") Long userId);
}

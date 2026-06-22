package com.ssafy.tripbaton.domain.bookmark.repository;

import com.ssafy.tripbaton.domain.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndRelayId(Long userId, Long relayId);

    java.util.Optional<Bookmark> findByUserIdAndRelayId(Long userId, Long relayId);

    @Query(
            value = "SELECT b FROM Bookmark b WHERE b.user.id = :userId ORDER BY b.createdAt DESC",
            countQuery = "SELECT COUNT(b) FROM Bookmark b WHERE b.user.id = :userId"
    )
    Page<Bookmark> findByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );
}

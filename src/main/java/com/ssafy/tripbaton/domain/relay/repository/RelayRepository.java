package com.ssafy.tripbaton.domain.relay.repository;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelayRepository extends JpaRepository<Relay, Long> {

    @Query("SELECT r FROM Relay r JOIN FETCH r.category " +
            "WHERE (:categoryId IS NULL OR r.category.id = :categoryId) " +
            "AND (:keyword IS NULL OR r.title LIKE %:keyword%) " +
            "ORDER BY r.createdAt DESC")
    Page<Relay> findAllByFilterOrderByLatest(@Param("categoryId") Long categoryId,
                                             @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM Relay r JOIN FETCH r.category " +
            "WHERE (:categoryId IS NULL OR r.category.id = :categoryId) " +
            "AND (:keyword IS NULL OR r.title LIKE %:keyword%) " +
            "ORDER BY r.participantCount DESC, r.createdAt DESC")
    Page<Relay> findAllByFilterOrderByPopular(@Param("categoryId") Long categoryId,
                                              @Param("keyword") String keyword, Pageable pageable);

    org.springframework.data.domain.Page<com.ssafy.tripbaton.domain.relay.entity.Relay>
    findAllByUserIdOrderByCreatedAtDesc(Long userId, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT r FROM Relay r JOIN FETCH r.category WHERE r.status IN ('ACTIVE', 'STALE') ORDER BY r.lastParticipatedAt DESC")
    List<Relay> findTop5Active(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT r FROM Relay r JOIN FETCH r.category ORDER BY r.participantCount DESC")
    List<Relay> findTopByParticipantCount(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT r FROM Relay r JOIN FETCH r.category " +
            "WHERE r.status <> 'ARCHIVED' " +
            "AND (:categoryId IS NULL OR r.category.id = :categoryId)")
    List<Relay> findActiveForMap(@Param("categoryId") Long categoryId);

    // ACTIVE → STALE: 7일 이상 참여 없음
    @Modifying
    @Query("UPDATE Relay r SET r.status = 'STALE' " +
            "WHERE r.status = 'ACTIVE' AND r.lastParticipatedAt < :staleThreshold")
    int updateActiveToStale(@Param("staleThreshold") java.time.LocalDateTime staleThreshold);

    // STALE → ARCHIVED: 30일 이상 참여 없음
    @Modifying
    @Query("UPDATE Relay r SET r.status = 'ARCHIVED' " +
            "WHERE r.status = 'STALE' AND r.lastParticipatedAt < :archivedThreshold")
    int updateStaleToArchived(@Param("archivedThreshold") java.time.LocalDateTime archivedThreshold);
}

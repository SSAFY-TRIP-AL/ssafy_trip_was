package com.ssafy.tripbaton.domain.relay.repository;

import com.ssafy.tripbaton.domain.relay.entity.Relay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelayRepository extends JpaRepository<Relay, Long> {

    @Query("SELECT r FROM Relay r JOIN FETCH r.category " +
           "WHERE (:categoryId IS NULL OR r.category.id = :categoryId) " +
           "AND (:keyword IS NULL OR r.title LIKE %:keyword%) " +
           "ORDER BY r.createdAt DESC")
    List<Relay> findAllByFilterOrderByLatest(@Param("categoryId") Long categoryId,
                                             @Param("keyword") String keyword);

    @Query("SELECT r FROM Relay r JOIN FETCH r.category " +
           "WHERE (:categoryId IS NULL OR r.category.id = :categoryId) " +
           "AND (:keyword IS NULL OR r.title LIKE %:keyword%) " +
           "ORDER BY r.participantCount DESC, r.createdAt DESC")
    List<Relay> findAllByFilterOrderByPopular(@Param("categoryId") Long categoryId,
                                              @Param("keyword") String keyword);
}

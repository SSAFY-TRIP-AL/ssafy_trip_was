package com.ssafy.tripbaton.domain.relay.entity;

import com.ssafy.tripbaton.domain.category.entity.Category;
import com.ssafy.tripbaton.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "relay")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Relay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String locationName;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 500)
    private String photoUrl;

    @Column(columnDefinition = "TEXT")
    private String content;

//    @Builder.Default
//    @Column(nullable = false)
//    private Boolean isBookmarked = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RelayStatus status = RelayStatus.ACTIVE;

    @Builder.Default
    @Column(nullable = false)
    private int participantCount = 0;

    private LocalDateTime lastParticipatedAt;

    public void addStep(LocalDateTime participatedAt) {
        this.participantCount++;
        this.lastParticipatedAt = participatedAt;
    }

//    public void setBookmarked(boolean bookmarked) {
//        this.isBookmarked = bookmarked;
//    }

    public void changeStatus(RelayStatus status) {
        this.status = status;
    }

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

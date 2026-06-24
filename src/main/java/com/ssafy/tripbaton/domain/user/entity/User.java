package com.ssafy.tripbaton.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String loginId;

    @Column(length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 500)
    private String profileImage;

    @Builder.Default
    @Column(nullable = false)
    private int participationCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private int createdCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private int likedCount = 0;

    @Column(length = 20)
    private String provider;

    @Column(length = 255)
    private String providerId;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

    public static User createOAuthUser(String provider, String providerId,
                                       String name, String email, String profileImage) {
        return User.builder()
                .provider(provider)
                .providerId(providerId)
                .name(name != null ? name : "소셜사용자")
                .email(email != null ? email : provider + "_" + providerId + "@social.local")
                .profileImage(profileImage)
                .build();
    }

    public void update(String name, String profileImage) {
        if (name != null) this.name = name;
        if (profileImage != null) this.profileImage = profileImage;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public boolean isSocialLoginUser() {
        return this.provider != null;
    }

    public void withdraw() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    // 릴레이 참여, 시작할 때 participationCount 증가 함수
    public void increaseParticipationCount() {
        this.participationCount++;
    }

    // 릴레이 생성 시 증가 함수
    public void increaseCreatedCount() {
        this.createdCount++;
    }

    // 좋아요 누를 때 좋아요 수 증가 함수
    public void increaseLikedCount() {
        this.likedCount++;
    }

    // 좋아요 취소할 때 좋아요 수 감소 함수
    public void decreaseLikedCount() {
        this.likedCount--;
    }

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
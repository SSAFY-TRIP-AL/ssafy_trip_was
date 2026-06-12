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

    @Column(length = 20)
    private String provider;

    @Column(length = 255)
    private String providerId;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
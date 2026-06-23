package com.ssafy.tripbaton.domain.user.dto;

import com.ssafy.tripbaton.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String name;
    private final String email;
    private final String profileImage;
    private final int participationCount;
    private final int createdCount;
    private final int likedCount;
    private final String provider;
    private final String createdAt;


    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.participationCount = user.getParticipationCount();
        this.provider = user.getProvider();
        this.createdAt = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.createdCount = user.getCreatedCount();
        this.likedCount = user.getLikedCount();
    }
}

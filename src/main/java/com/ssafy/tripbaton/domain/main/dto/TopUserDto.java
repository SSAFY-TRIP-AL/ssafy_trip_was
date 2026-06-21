package com.ssafy.tripbaton.domain.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopUserDto {
    private Long userId;
    private String profileImage;
    private String name;
    private int participationCount;
}

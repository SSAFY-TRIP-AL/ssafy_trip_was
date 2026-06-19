package com.ssafy.tripbaton.domain.ai.dto;

import lombok.Getter;

@Getter
public class AiRecommendRequestDto {

    private Long relayId;
    private String currentLocation;
    private String category;
    private String preference;
}

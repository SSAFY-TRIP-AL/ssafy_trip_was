package com.ssafy.tripbaton.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AiRecommendItemDto {

    private String locationName;
    private String reason;
}

package com.ssafy.tripbaton.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AiRecommendResponseDto {

    private List<AiRecommendItemDto> recommendations;
}

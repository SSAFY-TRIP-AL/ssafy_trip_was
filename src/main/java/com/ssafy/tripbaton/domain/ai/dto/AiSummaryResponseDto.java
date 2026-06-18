package com.ssafy.tripbaton.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AiSummaryResponseDto {

    private String locationName;
    private String summary;
    private List<String> highlights;
}

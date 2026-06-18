package com.ssafy.tripbaton.domain.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MarkerListResponseDto {

    private List<MarkerItemDto> markers;
}

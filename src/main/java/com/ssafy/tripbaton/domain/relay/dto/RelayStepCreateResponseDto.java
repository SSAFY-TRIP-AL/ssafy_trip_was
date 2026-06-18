package com.ssafy.tripbaton.domain.relay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelayStepCreateResponseDto {

    private String message;
    private Long stepId;
}

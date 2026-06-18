package com.ssafy.tripbaton.domain.relay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreatedRelayListResponseDto {

    private List<CreatedRelayListItemDto> relays;
}

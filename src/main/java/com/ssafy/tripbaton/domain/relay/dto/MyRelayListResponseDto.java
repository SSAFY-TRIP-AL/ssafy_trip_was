package com.ssafy.tripbaton.domain.relay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyRelayListResponseDto {

    private List<MyRelayListItemDto> relays;
}

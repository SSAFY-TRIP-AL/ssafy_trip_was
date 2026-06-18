package com.ssafy.tripbaton.domain.main.dto;

import com.ssafy.tripbaton.domain.relay.dto.RelayListItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MainResponseDto {

    private Long userCount;
    private Long relayCount;
    private List<TopUserDto> topUsers;
    private List<RelayListItemDto> relays;

}

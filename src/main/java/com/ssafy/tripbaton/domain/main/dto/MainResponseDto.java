package com.ssafy.tripbaton.domain.main.dto;

import com.ssafy.tripbaton.domain.relay.dto.ActiveRelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.HallOfFameResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListItemDto;
import com.ssafy.tripbaton.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MainResponseDto {

    private Long userCount;
    private Long relayCount;
//    private HallOfFameResponseDto hallOfFame;
    private ActiveRelayListResponseDto relays;
    private List<User> ranking;

}

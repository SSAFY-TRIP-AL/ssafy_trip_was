package com.ssafy.tripbaton.domain.main.service;

import com.ssafy.tripbaton.domain.main.dto.MainResponseDto;
import com.ssafy.tripbaton.domain.main.dto.TopUserDto;
import com.ssafy.tripbaton.domain.relay.dto.ActiveRelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.HallOfFameResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListItemDto;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.relay.service.RelayService;
import com.ssafy.tripbaton.domain.user.entity.User;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final UserRepository userRepository;
    private final RelayRepository relayRepository;
    private final RelayService relayService;

    public MainResponseDto getMain() {

        // 서비스에 등록된 사용자 수 반환
        long userCount = userRepository.count();

        // 서비스에 등록된 릴레이 수 반환
        long relayCount = relayRepository.count();

        // 릴레이 참여 상위 3명 정보 반환
//        HallOfFameResponseDto hallOfFame =
//                relayService.getHallOfFame();
        List<User> ranking = userRepository.findTop3ByOrderByParticipationCountDesc();

        // 서비스에 등록된 릴레이 4개 반환
        ActiveRelayListResponseDto relays = relayService.getActiveRelays();

        return new MainResponseDto(
                userCount,
                relayCount,
                relays,
                ranking
//                hallOfFame,

        );
    }
}

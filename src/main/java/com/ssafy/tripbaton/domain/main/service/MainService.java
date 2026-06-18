package com.ssafy.tripbaton.domain.main.service;

import com.ssafy.tripbaton.domain.main.dto.MainResponseDto;
import com.ssafy.tripbaton.domain.main.dto.TopUserDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListItemDto;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final UserRepository userRepository;
    private final RelayRepository relayRepository;

    public MainResponseDto getMain() {

        // 서비스에 등록된 사용자 수 반환
        long userCount = userRepository.count();

        // 서비스에 등록된 릴레이 수 반환
        long relayCount = relayRepository.count();

        // 릴레이 참여 상위 3명 정보 반환
        List<TopUserDto> topUsers = userRepository
                .findTop3ByOrderByParticipationCountDesc()
                .stream()
                .map(user -> new TopUserDto(
                        user.getId(),
                        user.getProfileImage(),
                        user.getName(),
                        user.getParticipationCount()
                )).toList();

        // 서비스에 등록된 릴레이 4개 반환
        List<RelayListItemDto> relays = relayRepository
                .findTop4ByOrderByCreatedAtDesc()
                .stream()
                .map(RelayListItemDto::new)
                .toList();

        return new MainResponseDto(
                userCount,
                relayCount,
                topUsers,
                relays
        );
    }
}

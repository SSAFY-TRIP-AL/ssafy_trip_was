package com.ssafy.tripbaton.domain.relay.service;

import com.ssafy.tripbaton.domain.category.entity.Category;
import com.ssafy.tripbaton.domain.category.repository.CategoryRepository;
import com.ssafy.tripbaton.domain.relay.dto.RelayCreateRequestDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayCreateResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayDetailResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListItemDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.ActiveRelayListItemDto;
import com.ssafy.tripbaton.domain.relay.dto.ActiveRelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.HallOfFameEntryDto;
import com.ssafy.tripbaton.domain.relay.dto.HallOfFameRelayDto;
import com.ssafy.tripbaton.domain.relay.dto.HallOfFameResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayRouteResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayRouteStepDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayStepCreateRequestDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayStepCreateResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayStepDto;
import com.ssafy.tripbaton.domain.relay.entity.Relay;
import com.ssafy.tripbaton.domain.relay.entity.RelayStatus;
import com.ssafy.tripbaton.domain.relay.entity.RelayStep;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.relay.repository.RelayStepRepository;
import com.ssafy.tripbaton.domain.user.entity.User;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelayService {

    private final RelayRepository relayRepository;
    private final RelayStepRepository relayStepRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public RelayCreateResponseDto createRelay(Long userId, RelayCreateRequestDto dto) {
        if (dto.getTitle() == null || dto.getCategoryId() == null
//                || dto.getLocationName() == null
                || dto.getLatitude() == null || dto.getLongitude() == null) {
            throw new CustomException(ErrorCode.MISSING_RELAY_FIELDS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.MISSING_RELAY_FIELDS));

        Relay relay = Relay.builder()
                .user(user)
                .category(category)
                .title(dto.getTitle())
//                .locationName(dto.getAddress())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
//                .photoUrl(dto.getPhotoUrl())
                .content(dto.getContent())
                .participantCount(1)
                .lastParticipatedAt(LocalDateTime.now())
                .build();

        Relay saved = relayRepository.save(relay);

        RelayStep firstStep = RelayStep.builder()
                .relay(saved)
                .user(user)
                .stepOrder(1)
                .locationName(dto.getAddress())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
//                .photoUrl(dto.getPhotoUrl())
                .content(dto.getContent())
                .build();

        relayStepRepository.save(firstStep);

        // 릴레이 생성 시 참여 +1
        user.increaseParticipationCount();

        return new RelayCreateResponseDto("릴레이가 등록되었습니다.", saved.getId());
    }

    @Transactional(readOnly = true)
    public RelayListResponseDto getRelays(Long categoryId, String sort, String keyword) {
        List<Relay> relays = "popular".equals(sort)
                ? relayRepository.findAllByFilterOrderByPopular(categoryId, keyword)
                : relayRepository.findAllByFilterOrderByLatest(categoryId, keyword);

        List<RelayListItemDto> items = relays.stream()
                .map(RelayListItemDto::new)
                .toList();

        return new RelayListResponseDto(items);
    }

    @Transactional(readOnly = true)
    public RelayDetailResponseDto getRelay(Long relayId) {
        Relay relay = relayRepository.findById(relayId)
                .orElseThrow(() -> new CustomException(ErrorCode.RELAY_NOT_FOUND));

        List<RelayStepDto> steps = relayStepRepository.findByRelayIdOrderByStepOrderAsc(relayId)
                .stream()
                .map(RelayStepDto::new)
                .toList();

        return new RelayDetailResponseDto(relay, steps);
    }

    @Transactional(readOnly = true)
    public HallOfFameResponseDto getHallOfFame() {
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);

        List<Relay> topByParticipants = relayRepository.findTopByParticipantCount(
                org.springframework.data.domain.PageRequest.of(0, 3));

        List<HallOfFameEntryDto> entries = new java.util.ArrayList<>();

        for (int i = 0; i < topByParticipants.size(); i++) {
            Relay relay = topByParticipants.get(i);
            entries.add(new HallOfFameEntryDto(i + 1, "MOST_PARTICIPANTS",
                    new HallOfFameRelayDto(relay, relay.getParticipantCount())));
        }

        return new HallOfFameResponseDto(weekStart, entries);
    }

    @Transactional(readOnly = true)
    public ActiveRelayListResponseDto getActiveRelays() {
        List<ActiveRelayListItemDto> items = relayRepository
                .findTop5Active(org.springframework.data.domain.PageRequest.of(0, 4))
                .stream()
                .map(ActiveRelayListItemDto::new)
                .toList();
        return new ActiveRelayListResponseDto(items);
    }

    @Transactional(readOnly = true)
    public RelayRouteResponseDto getRelayRoute(Long relayId) {
        Relay relay = relayRepository.findById(relayId)
                .orElseThrow(() -> new CustomException(ErrorCode.RELAY_NOT_FOUND));

        List<RelayRouteStepDto> steps = relayStepRepository.findByRelayIdOrderByStepOrderAsc(relayId)
                .stream()
                .map(RelayRouteStepDto::new)
                .toList();

        return new RelayRouteResponseDto(relay, steps);
    }

    @Transactional
    public RelayStepCreateResponseDto joinRelay(Long userId, Long relayId, RelayStepCreateRequestDto dto) {
        if (dto.getLocationName() == null || dto.getLatitude() == null || dto.getLongitude() == null) {
            throw new CustomException(ErrorCode.MISSING_STEP_FIELDS);
        }

        Relay relay = relayRepository.findById(relayId)
                .orElseThrow(() -> new CustomException(ErrorCode.RELAY_NOT_FOUND));

        if (relay.getStatus() == RelayStatus.ARCHIVED) {
            throw new CustomException(ErrorCode.RELAY_ARCHIVED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        int nextOrder = relayStepRepository.countByRelayId(relayId) + 1;

        RelayStep step = RelayStep.builder()
                .relay(relay)
                .user(user)
                .stepOrder(nextOrder)
                .locationName(dto.getLocationName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .photoUrl(dto.getPhotoUrl())
                .content(dto.getContent())
                .build();

        RelayStep saved = relayStepRepository.save(step);

        // 릴레이 참여 시 참여 +1
        user.increaseParticipationCount();

        LocalDateTime now = LocalDateTime.now();
        relay.addStep(now);

        return new RelayStepCreateResponseDto("릴레이에 참여하였습니다.", saved.getId());
    }
}

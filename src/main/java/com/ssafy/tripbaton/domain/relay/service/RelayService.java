package com.ssafy.tripbaton.domain.relay.service;

import com.ssafy.tripbaton.domain.category.entity.Category;
import com.ssafy.tripbaton.domain.category.repository.CategoryRepository;
import com.ssafy.tripbaton.domain.relay.dto.RelayCreateRequestDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayCreateResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayDetailResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListItemDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayStepDto;
import com.ssafy.tripbaton.domain.relay.entity.Relay;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.relay.repository.RelayStepRepository;
import com.ssafy.tripbaton.domain.user.entity.User;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                || dto.getLocationName() == null
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
                .locationName(dto.getLocationName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .photoUrl(dto.getPhotoUrl())
                .content(dto.getContent())
                .build();

        Relay saved = relayRepository.save(relay);
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
}

package com.ssafy.tripbaton.domain.relay.service;

import com.ssafy.tripbaton.domain.category.entity.Category;
import com.ssafy.tripbaton.domain.category.repository.CategoryRepository;
import com.ssafy.tripbaton.domain.relay.dto.RelayCreateRequestDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayCreateResponseDto;
import com.ssafy.tripbaton.domain.relay.entity.Relay;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.user.entity.User;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RelayService {

    private final RelayRepository relayRepository;
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
}

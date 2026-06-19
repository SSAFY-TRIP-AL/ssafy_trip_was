package com.ssafy.tripbaton.domain.ai.controller;

import com.ssafy.tripbaton.domain.ai.dto.AiRecommendRequestDto;
import com.ssafy.tripbaton.domain.ai.dto.AiRecommendResponseDto;
import com.ssafy.tripbaton.domain.ai.dto.AiSummaryResponseDto;
import com.ssafy.tripbaton.domain.ai.service.AiService;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Tag(name = "ai")
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final RelayRepository relayRepository;

    @GetMapping("/summary")
    public ResponseEntity<AiSummaryResponseDto> getSummary(
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) String category) {
        if (locationName == null || locationName.isBlank()) {
            throw new CustomException(ErrorCode.MISSING_LOCATION_NAME);
        }
        return ResponseEntity.ok(aiService.getSummary(locationName, category));
    }

    @PostMapping("/recommend")
    public ResponseEntity<AiRecommendResponseDto> getRecommendations(Authentication authentication,
                                                                      @RequestBody AiRecommendRequestDto dto) {
        if (dto.getRelayId() == null || dto.getCurrentLocation() == null || dto.getCurrentLocation().isBlank()) {
            throw new CustomException(ErrorCode.MISSING_RECOMMEND_FIELDS);
        }
        if (!relayRepository.existsById(dto.getRelayId())) {
            throw new CustomException(ErrorCode.RELAY_NOT_FOUND);
        }
        return ResponseEntity.ok(aiService.getRecommendations(
                dto.getCurrentLocation(), dto.getCategory(), dto.getPreference()));
    }
}

package com.ssafy.tripbaton.domain.ai.controller;

import com.ssafy.tripbaton.domain.ai.dto.AiSummaryResponseDto;
import com.ssafy.tripbaton.domain.ai.service.AiService;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ai")
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/summary")
    public ResponseEntity<AiSummaryResponseDto> getSummary(
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) String category) {
        if (locationName == null || locationName.isBlank()) {
            throw new CustomException(ErrorCode.MISSING_LOCATION_NAME);
        }
        return ResponseEntity.ok(aiService.getSummary(locationName, category));
    }
}

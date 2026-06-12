package com.ssafy.tripbaton.domain.relay.controller;

import com.ssafy.tripbaton.domain.relay.dto.RelayCreateRequestDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayCreateResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.service.RelayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "relay")
@RestController
@RequestMapping("/api/relays")
@RequiredArgsConstructor
public class RelayController {

    private final RelayService relayService;

    @GetMapping
    public ResponseEntity<RelayListResponseDto> getRelays(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(relayService.getRelays(categoryId, sort, keyword));
    }

    @PostMapping
    public ResponseEntity<RelayCreateResponseDto> createRelay(Authentication authentication,
                                                              @RequestBody RelayCreateRequestDto dto) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(relayService.createRelay(userId, dto));
    }
}

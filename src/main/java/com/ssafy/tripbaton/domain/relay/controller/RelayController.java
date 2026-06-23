package com.ssafy.tripbaton.domain.relay.controller;

import com.ssafy.tripbaton.domain.relay.dto.RelayCreateRequestDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayCreateResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayDetailResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.ActiveRelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayRouteResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayStepCreateRequestDto;
import com.ssafy.tripbaton.domain.relay.dto.RelayStepCreateResponseDto;
import com.ssafy.tripbaton.domain.relay.service.RelayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/active")
    public ResponseEntity<ActiveRelayListResponseDto> getActiveRelays() {
        return ResponseEntity.ok(relayService.getActiveRelays());
    }

    @GetMapping
    public ResponseEntity<RelayListResponseDto> getRelays(
            Authentication authentication,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false, defaultValue = "latest") String orderBy,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(relayService.getRelays(userId, categoryId, orderBy, keyword, pageable));
    }

    @GetMapping("/{relayId}")
    public ResponseEntity<RelayDetailResponseDto> getRelay(Authentication authentication, @PathVariable Long relayId) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(relayService.getRelay(userId,relayId));
    }

    @GetMapping("/{relayId}/steps")
    public ResponseEntity<RelayRouteResponseDto> getRelayRoute(@PathVariable Long relayId) {
        return ResponseEntity.ok(relayService.getRelayRoute(relayId));
    }

    @PostMapping("/{relayId}/steps")
    public ResponseEntity<RelayStepCreateResponseDto> joinRelay(Authentication authentication,
                                                                @PathVariable Long relayId,
                                                                @RequestBody RelayStepCreateRequestDto dto) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(relayService.joinRelay(userId, relayId, dto));
    }

    @PostMapping
    public ResponseEntity<RelayCreateResponseDto> createRelay(Authentication authentication,
                                                              @RequestBody RelayCreateRequestDto dto) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(relayService.createRelay(userId, dto));
    }
}

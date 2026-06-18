package com.ssafy.tripbaton.domain.main.controller;

import com.ssafy.tripbaton.domain.relay.dto.HallOfFameResponseDto;
import com.ssafy.tripbaton.domain.relay.service.RelayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "main")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final RelayService relayService;

    @GetMapping("/hall-of-fame")
    public ResponseEntity<HallOfFameResponseDto> getHallOfFame() {
        return ResponseEntity.ok(relayService.getHallOfFame());
    }
}

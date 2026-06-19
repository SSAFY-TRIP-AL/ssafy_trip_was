package com.ssafy.tripbaton.domain.map.controller;

import com.ssafy.tripbaton.domain.map.dto.MarkerListResponseDto;
import com.ssafy.tripbaton.domain.map.service.MapService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "map")
@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/markers")
    public ResponseEntity<MarkerListResponseDto> getMarkers(
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(mapService.getMarkers(categoryId));
    }
}

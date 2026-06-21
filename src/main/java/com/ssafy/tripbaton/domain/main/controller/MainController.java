package com.ssafy.tripbaton.domain.main.controller;


import com.ssafy.tripbaton.domain.main.dto.MainResponseDto;
import com.ssafy.tripbaton.domain.main.service.MainService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "main")
@RestController

@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping
    public ResponseEntity<MainResponseDto> getMain() {
        return ResponseEntity.ok(
                mainService.getMain()
        );
    }


}

package com.ssafy.tripbaton.domain.auth.controller;

import com.ssafy.tripbaton.domain.user.dto.LoginRequestDto;
import com.ssafy.tripbaton.domain.user.dto.LoginResponseDto;
import com.ssafy.tripbaton.domain.user.dto.SignupRequestDto;
import com.ssafy.tripbaton.domain.user.service.UserService;
import com.ssafy.tripbaton.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequestDto dto) {
        userService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponseDto> reissue(
            @RequestHeader("Authorization") String authorizationHeader) {
        String refreshToken = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.reissue(refreshToken));
    }
}
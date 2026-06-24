package com.ssafy.tripbaton.domain.auth.controller;

import com.ssafy.tripbaton.domain.auth.dto.OAuthRequestDto;
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
import org.springframework.security.core.Authentication;
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

    @PostMapping("/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestBody OAuthRequestDto dto) {
        return ResponseEntity.ok(userService.oauthLogin("kakao", dto.getCode(), dto.getRedirectUri()));
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestBody OAuthRequestDto dto) {
        return ResponseEntity.ok(userService.oauthLogin("google", dto.getCode(), dto.getRedirectUri()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponseDto> reissue(
            @RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("리이슈");
        String refreshToken = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.reissue(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.logout(userId);
        return ResponseEntity.ok(ApiResponse.of("로그아웃이 완료되었습니다."));
    }
}
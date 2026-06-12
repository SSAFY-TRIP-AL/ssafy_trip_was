package com.ssafy.tripbaton.domain.user.controller;

import com.ssafy.tripbaton.domain.user.dto.UserResponseDto;
import com.ssafy.tripbaton.domain.user.dto.UserUpdateRequestDto;
import com.ssafy.tripbaton.domain.user.service.UserService;
import com.ssafy.tripbaton.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getMe(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateMe(Authentication authentication,
                                                @RequestBody UserUpdateRequestDto dto) {
        Long userId = (Long) authentication.getPrincipal();
        userService.updateMe(userId, dto);
        return ResponseEntity.ok(ApiResponse.of("정보가 수정되었습니다."));
    }
}

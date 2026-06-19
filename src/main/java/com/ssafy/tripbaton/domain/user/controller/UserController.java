package com.ssafy.tripbaton.domain.user.controller;

import com.ssafy.tripbaton.domain.bookmark.dto.BookmarkListResponseDto;
import com.ssafy.tripbaton.domain.bookmark.service.BookmarkService;
import com.ssafy.tripbaton.domain.relay.dto.CreatedRelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.MyRelayListResponseDto;
import com.ssafy.tripbaton.domain.user.dto.ChangePasswordRequestDto;
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
    private final BookmarkService bookmarkService;

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

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse> changePassword(Authentication authentication,
                                                      @RequestBody ChangePasswordRequestDto dto) {
        Long userId = (Long) authentication.getPrincipal();
        userService.changePassword(userId, dto);
        return ResponseEntity.ok(ApiResponse.of("비밀번호가 변경되었습니다."));
    }

    @GetMapping("/me/bookmarks")
    public ResponseEntity<BookmarkListResponseDto> getMyBookmarks(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(bookmarkService.getMyBookmarks(userId));
    }

    @GetMapping("/me/relays/created")
    public ResponseEntity<CreatedRelayListResponseDto> getMyCreatedRelays(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getMyCreatedRelays(userId));
    }

    @GetMapping("/me/relays")
    public ResponseEntity<MyRelayListResponseDto> getMyRelays(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getMyRelays(userId));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse> withdraw(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.withdraw(userId);
        return ResponseEntity.ok(ApiResponse.of("회원 탈퇴가 완료되었습니다."));
    }
}

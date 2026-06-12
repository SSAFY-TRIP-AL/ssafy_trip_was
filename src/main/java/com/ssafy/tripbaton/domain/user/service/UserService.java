package com.ssafy.tripbaton.domain.user.service;

import com.ssafy.tripbaton.domain.auth.entity.RefreshToken;
import com.ssafy.tripbaton.domain.auth.repository.RefreshTokenRepository;
import com.ssafy.tripbaton.domain.user.dto.LoginRequestDto;
import com.ssafy.tripbaton.domain.user.dto.LoginResponseDto;
import com.ssafy.tripbaton.domain.user.dto.SignupRequestDto;
import com.ssafy.tripbaton.domain.user.entity.User;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import com.ssafy.tripbaton.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        if (userRepository.existsByLoginId(dto.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .profileImage(dto.getProfileImage())
                .build();

        userRepository.save(user);
    }

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 기존 refresh token 삭제 후 새로 저장
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshExpiration() / 1000))
                .createdAt(LocalDateTime.now())
                .build());

        return new LoginResponseDto(accessToken, user.getName(), "로그인이 완료되었습니다.");
    }

    @Transactional
    public LoginResponseDto reissue(String refreshToken) {
        // 토큰 유효성 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtUtil.getUserId(refreshToken);

        // DB에 저장된 refresh token과 비교
        RefreshToken savedToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (!savedToken.getToken().equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 새 access token 발급
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        return new LoginResponseDto(newAccessToken, user.getName(), "토큰이 재발급되었습니다.");
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
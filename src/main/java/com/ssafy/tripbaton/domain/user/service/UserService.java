package com.ssafy.tripbaton.domain.user.service;

import com.ssafy.tripbaton.domain.auth.dto.OAuthUserProfile;
import com.ssafy.tripbaton.domain.auth.entity.RefreshToken;
import com.ssafy.tripbaton.domain.auth.repository.RefreshTokenRepository;
import com.ssafy.tripbaton.domain.auth.service.OAuthService;
import com.ssafy.tripbaton.domain.relay.dto.CreatedRelayListItemDto;
import com.ssafy.tripbaton.domain.relay.dto.CreatedRelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.dto.MyRelayListItemDto;
import com.ssafy.tripbaton.domain.relay.dto.MyRelayListResponseDto;
import com.ssafy.tripbaton.domain.relay.repository.RelayRepository;
import com.ssafy.tripbaton.domain.relay.repository.RelayStepRepository;
import com.ssafy.tripbaton.domain.user.dto.ChangePasswordRequestDto;
import com.ssafy.tripbaton.domain.user.dto.LoginRequestDto;
import com.ssafy.tripbaton.domain.user.dto.LoginResponseDto;
import com.ssafy.tripbaton.domain.user.dto.SignupRequestDto;
import com.ssafy.tripbaton.domain.user.dto.UserResponseDto;
import com.ssafy.tripbaton.domain.user.dto.UserUpdateRequestDto;
import com.ssafy.tripbaton.domain.user.entity.User;
import com.ssafy.tripbaton.domain.user.repository.UserRepository;
import com.ssafy.tripbaton.global.exception.CustomException;
import com.ssafy.tripbaton.global.exception.ErrorCode;
import com.ssafy.tripbaton.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RelayStepRepository relayStepRepository;
    private final RelayRepository relayRepository;

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
    private final OAuthService oAuthService;

    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        // 탈퇴한 사용자면 로그인 차단
        if(user.isDeleted()){
            throw new CustomException(ErrorCode.DELETED_USER);
        }

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

        return new LoginResponseDto(accessToken, user.getName(), user.getProfileImage(), "로그인이 완료되었습니다.");
    }

    @Transactional
    public LoginResponseDto oauthLogin(String provider, String code, String redirectUri) {
        OAuthUserProfile profile = oAuthService.getUserProfile(provider, code, redirectUri);

        User user = userRepository.findByProviderAndProviderId(provider, profile.getProviderId())
                .orElseGet(() -> userRepository.save(
                        User.createOAuthUser(provider, profile.getProviderId(),
                                profile.getName(), profile.getEmail(), profile.getProfileImage())));

        if (user.isDeleted()) {
            throw new CustomException(ErrorCode.DELETED_USER);
        }

        // 매 로그인마다 소셜 프로필(닉네임/이미지) 최신화 (null 값은 기존 유지)
        user.update(profile.getName(), profile.getProfileImage());

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshExpiration() / 1000))
                .createdAt(LocalDateTime.now())
                .build());

        return new LoginResponseDto(accessToken, user.getName(), user.getProfileImage(), "로그인이 완료되었습니다.");
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

        return new LoginResponseDto(newAccessToken, user.getName(), user.getProfileImage(), "토큰이 재발급되었습니다.");
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
        return new UserResponseDto(user);
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
        user.withdraw();
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequestDto dto) {
        if (!dto.getNewPassword().equals(dto.getNewPasswordConfirm())) {
            throw new CustomException(ErrorCode.NEW_PASSWORD_MISMATCH);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
        if (user.isSocialLoginUser()) {
            throw new CustomException(ErrorCode.SOCIAL_LOGIN_USER);
        }
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }
        user.changePassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    @Transactional(readOnly = true)
    public MyRelayListResponseDto getMyRelays(Long userId, Pageable pageable) {

        Page<MyRelayListItemDto> page =
                relayStepRepository.findDistinctRelaysByUserId(userId, pageable)
                        .map(MyRelayListItemDto::new);

        return new MyRelayListResponseDto(page);
    }

    @Transactional(readOnly = true)
    public CreatedRelayListResponseDto getMyCreatedRelays(Long userId, Pageable pageable) {

        Page<CreatedRelayListItemDto> page =
                relayRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
                        .map(CreatedRelayListItemDto::new);

        return new CreatedRelayListResponseDto(page);
    }

    @Transactional
    public void updateMe(Long userId, UserUpdateRequestDto dto) {
        if (dto.getName() == null && dto.getProfileImage() == null) {
            throw new CustomException(ErrorCode.NO_UPDATE_FIELDS);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
        user.update(dto.getName(), dto.getProfileImage());
    }
}
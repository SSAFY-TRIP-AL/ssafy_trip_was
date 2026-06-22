package com.ssafy.tripbaton.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MISSING_FIELDS(HttpStatus.BAD_REQUEST, "필수 필드(loginId, password, name, email, nickname)가 필요합니다.", "MISSING_FIELDS"),
    NO_UPDATE_FIELDS(HttpStatus.BAD_REQUEST, "수정할 항목이 없습니다.", "MISSING_FIELDS"),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.", "DUPLICATE_LOGIN_ID"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.", "DUPLICATE_EMAIL"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", "PASSWORD_MISMATCH"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.", "INVALID_CREDENTIALS"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다.", "INVALID_REFRESH_TOKEN"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", "INVALID_TOKEN"),
    SOCIAL_LOGIN_USER(HttpStatus.FORBIDDEN, "소셜 로그인 사용자는 비밀번호를 변경할 수 없습니다.", "SOCIAL_LOGIN_USER"),
    INVALID_CURRENT_PASSWORD(HttpStatus.CONFLICT, "현재 비밀번호가 올바르지 않습니다.", "INVALID_CURRENT_PASSWORD"),
    NEW_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "새 비밀번호가 일치하지 않습니다.", "PASSWORD_MISMATCH"),
    MISSING_RELAY_FIELDS(HttpStatus.BAD_REQUEST, "필수 필드(title, categoryId, locationName, latitude, longitude)가 필요합니다.", "MISSING_FIELDS"),
    RELAY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 릴레이입니다.", "RELAY_NOT_FOUND"),
    RELAY_USING(HttpStatus.CONFLICT, "다른 사용자가 참여 중인 릴레이에는 참여할 수 없습니다.", "RELAY_USING"),
    RELAY_ARCHIVED(HttpStatus.CONFLICT, "아카이브된 릴레이에는 참여할 수 없습니다.", "RELAY_ARCHIVED"),
    ALREADY_BOOKMARKED(HttpStatus.CONFLICT, "이미 찜한 릴레이입니다.", "ALREADY_BOOKMARKED"),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "찜한 릴레이가 존재하지 않습니다.", "BOOKMARK_NOT_FOUND"),
    MISSING_STEP_FIELDS(HttpStatus.BAD_REQUEST, "필수 필드(locationName, latitude, longitude)가 필요합니다.", "MISSING_FIELDS"),
    AI_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 요청 처리 중 오류가 발생했습니다.", "AI_REQUEST_FAILED"),
    MISSING_LOCATION_NAME(HttpStatus.BAD_REQUEST, "locationName은 필수입니다.", "MISSING_LOCATION_NAME"),
    MISSING_RECOMMEND_FIELDS(HttpStatus.BAD_REQUEST, "필수 필드(relayId, currentLocation)가 필요합니다.", "MISSING_FIELDS");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
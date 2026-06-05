package com.ssafy.tripbaton.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MISSING_FIELDS(HttpStatus.BAD_REQUEST, "필수 필드(loginId, password, name, email, nickname)가 필요합니다.", "MISSING_FIELDS"),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.", "DUPLICATE_LOGIN_ID"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.", "DUPLICATE_EMAIL"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.", "DUPLICATE_NICKNAME");

    private final HttpStatus status;
    private final String message;
    private final String code;
}
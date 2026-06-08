package com.ssafy.tripbaton.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
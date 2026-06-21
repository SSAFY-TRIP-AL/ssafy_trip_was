package com.ssafy.tripbaton.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private String accessToken;
    private String name;
    private String profileImage;
    private String message;


}
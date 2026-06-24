package com.ssafy.tripbaton.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserProfile {

    private String providerId;
    private String name;
    private String email;
    private String profileImage;
}

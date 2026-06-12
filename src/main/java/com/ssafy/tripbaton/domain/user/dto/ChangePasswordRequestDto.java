package com.ssafy.tripbaton.domain.user.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {

    private String currentPassword;
    private String newPassword;
    private String newPasswordConfirm;
}

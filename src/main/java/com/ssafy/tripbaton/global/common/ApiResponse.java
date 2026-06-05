package com.ssafy.tripbaton.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {

    private String message;

    public static ApiResponse of(String message) {
        return new ApiResponse(message);
    }
}
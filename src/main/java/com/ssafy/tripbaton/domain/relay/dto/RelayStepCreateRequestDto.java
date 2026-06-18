package com.ssafy.tripbaton.domain.relay.dto;

import lombok.Getter;

@Getter
public class RelayStepCreateRequestDto {

    private String locationName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String photoUrl;
    private String content;
}

package com.ssafy.tripbaton.domain.relay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelayCreateRequestDto {

    private String title;
    private Long categoryId;
//    private String locationName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String photoUrl;
    private String content;
}

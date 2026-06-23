package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.RelayStep;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RelayStepDto {

    private final int stepOrder;
    private final String userName;
    private final String locationName;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final String photoUrl;
    private final String content;
    private final LocalDateTime createdAt;

    public RelayStepDto(RelayStep step) {
        this.stepOrder = step.getStepOrder();
        this.userName = step.getUser().getName();
        this.locationName = step.getLocationName();
        this.address = step.getAddress();
        this.latitude = step.getLatitude();
        this.longitude = step.getLongitude();
        this.photoUrl = step.getPhotoUrl();
        this.content = step.getContent();
        this.createdAt = step.getCreatedAt();
    }
}

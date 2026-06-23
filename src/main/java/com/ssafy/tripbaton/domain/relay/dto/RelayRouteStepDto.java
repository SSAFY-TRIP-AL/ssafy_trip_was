package com.ssafy.tripbaton.domain.relay.dto;

import com.ssafy.tripbaton.domain.relay.entity.RelayStep;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RelayRouteStepDto {

    private final String userName;
    private final int stepOrder;
    private final String locationName;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final LocalDateTime createdAt;


    public RelayRouteStepDto(RelayStep step) {
        this.stepOrder = step.getStepOrder();
        this.locationName = step.getLocationName();
        this.address = step.getAddress();
        this.latitude = step.getLatitude();
        this.longitude = step.getLongitude();
        this.createdAt = step.getCreatedAt();
        this.userName = step.getUser().getName();
    }
}

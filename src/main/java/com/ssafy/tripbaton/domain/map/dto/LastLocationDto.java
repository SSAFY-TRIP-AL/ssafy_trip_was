package com.ssafy.tripbaton.domain.map.dto;

import com.ssafy.tripbaton.domain.relay.entity.RelayStep;
import lombok.Getter;

@Getter
public class LastLocationDto {

    private final String locationName;
    private final Double latitude;
    private final Double longitude;

    public LastLocationDto(RelayStep step) {
        this.locationName = step.getLocationName();
        this.latitude = step.getLatitude();
        this.longitude = step.getLongitude();
    }
}

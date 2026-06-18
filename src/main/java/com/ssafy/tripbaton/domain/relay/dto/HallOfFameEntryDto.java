package com.ssafy.tripbaton.domain.relay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HallOfFameEntryDto {

    private final int rank;
    private final String type;
    private final HallOfFameRelayDto relay;
}

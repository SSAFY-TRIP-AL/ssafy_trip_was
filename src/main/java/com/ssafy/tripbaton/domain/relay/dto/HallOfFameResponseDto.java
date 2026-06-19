package com.ssafy.tripbaton.domain.relay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class HallOfFameResponseDto {

    private final LocalDate weekStart;
    private final List<HallOfFameEntryDto> hallOfFame;
}

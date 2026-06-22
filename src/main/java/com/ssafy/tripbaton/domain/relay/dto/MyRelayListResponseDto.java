package com.ssafy.tripbaton.domain.relay.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MyRelayListResponseDto {

    private final List<MyRelayListItemDto> relays;

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public MyRelayListResponseDto(org.springframework.data.domain.Page<MyRelayListItemDto> page) {
        this.relays = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}

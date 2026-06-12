package com.ssafy.tripbaton.domain.category.dto;

import com.ssafy.tripbaton.domain.category.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {

    private final Long id;
    private final String name;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}

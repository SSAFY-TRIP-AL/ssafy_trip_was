package com.ssafy.tripbaton.domain.category.service;

import com.ssafy.tripbaton.domain.category.dto.CategoryResponseDto;
import com.ssafy.tripbaton.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDto::new)
                .toList();
    }
}

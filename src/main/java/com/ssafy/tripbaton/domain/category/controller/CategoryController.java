package com.ssafy.tripbaton.domain.category.controller;

import com.ssafy.tripbaton.domain.category.dto.CategoryResponseDto;
import com.ssafy.tripbaton.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "category")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Map<String, List<CategoryResponseDto>>> getCategories() {
        return ResponseEntity.ok(Map.of("categories", categoryService.getCategories()));
    }
}

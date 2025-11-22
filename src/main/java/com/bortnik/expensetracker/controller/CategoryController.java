package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.category.*;
import com.bortnik.expensetracker.security.service.UserDetailsImpl;
import com.bortnik.expensetracker.service.CategoryService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryDTO> createCategory(
            @Valid
            @RequestBody
            CategoryCreateRequestDTO categoryCreateRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getId();
        CategoryCreateDTO categoryCreateDTO = CategoryCreateDTO.builder()
                .userId(userId)
                .name(categoryCreateRequestDTO.getName())
                .build();
        CategoryDTO category = categoryService.saveCategory(categoryCreateDTO);
        return ApiResponseFactory.success(category);
    }

    @PatchMapping
    public ApiResponse<CategoryDTO> updateCategory(
            @Valid
            @RequestBody
            CategoryUpdateRequestDTO categoryUpdateRequestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        UUID userId = userDetails.getId();
        CategoryUpdateDTO categoryUpdateDTO = CategoryUpdateDTO.builder()
                .userId(userId)
                .id(categoryUpdateRequestDTO.getId())
                .name(categoryUpdateRequestDTO.getName())
                .build();
        CategoryDTO category = categoryService.updateCategory(categoryUpdateDTO);
        return ApiResponseFactory.success(category);
    }

    @GetMapping("/all")
    public ApiResponse<Page<CategoryDTO>> getAllCategories(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        UUID userId = userDetails.getId();
        Page<CategoryDTO> categories = categoryService.getAllByUserId(userId, pageable);
        return ApiResponseFactory.success(categories);
    }
}
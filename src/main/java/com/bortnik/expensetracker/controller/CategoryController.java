package com.bortnik.expensetracker.controller;

import com.bortnik.expensetracker.dto.ApiResponse;
import com.bortnik.expensetracker.dto.category.*;
import com.bortnik.expensetracker.service.CategoryService;
import com.bortnik.expensetracker.service.UserService;
import com.bortnik.expensetracker.util.ApiResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    @PostMapping
    public ApiResponse<CategoryDTO> createCategory(
            @Valid
            @RequestBody
            CategoryCreateRequestDTO categoryCreateRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
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
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        CategoryUpdateDTO categoryUpdateDTO = CategoryUpdateDTO.builder()
                .userId(userId)
                .id(categoryUpdateRequestDTO.getId())
                .name(categoryUpdateRequestDTO.getName())
                .build();
        CategoryDTO category = categoryService.updateCategory(categoryUpdateDTO);
        return ApiResponseFactory.success(category);
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryDTO>> getAllCategories(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = userService.getUserByUsername(userDetails.getUsername()).getId();
        List<CategoryDTO> categories = categoryService.getAllByUserId(userId);
        return ApiResponseFactory.success(categories);
    }
}
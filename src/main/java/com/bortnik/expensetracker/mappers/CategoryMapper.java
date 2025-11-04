package com.bortnik.expensetracker.mappers;

import com.bortnik.expensetracker.dto.category.CategoryDTO;
import com.bortnik.expensetracker.dto.category.CategoryCreateDTO;
import com.bortnik.expensetracker.entities.Category;

public class CategoryMapper {
    public static CategoryDTO toDto(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .userId(category.getUserId())
                .name(category.getName())
                .build();
    }

    public static Category toEntity(CategoryCreateDTO categoryCreateDTO) {
        return Category.builder()
                .name(categoryCreateDTO.getName())
                .userId(categoryCreateDTO.getUserId())
                .build();
    }
}

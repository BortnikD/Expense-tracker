package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.category.CategoryDTO;
import com.bortnik.expensetracker.dto.category.CategoryUpdate;
import com.bortnik.expensetracker.dto.category.CreateCategoryDTO;
import com.bortnik.expensetracker.entities.Category;
import com.bortnik.expensetracker.exceptions.category.CategoryAlreadyExists;
import com.bortnik.expensetracker.exceptions.category.CategoryNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.mappers.CategoryMapper;
import com.bortnik.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDTO saveCategory(CreateCategoryDTO createCategoryDTO) {
        if (categoryRepository.existsByUserIdAndName(createCategoryDTO.getUserId(), createCategoryDTO.getName())) {
            throw new CategoryAlreadyExists("Category with name " + createCategoryDTO.getName() +
                    " and userId " + createCategoryDTO.getUserId() + " already exists");
        }
        return CategoryMapper.toDto(
                categoryRepository.save(CategoryMapper.toEntity(createCategoryDTO))
        );
    }

    public CategoryDTO updateCategory(CategoryUpdate categoryUpdate) {
        Category category = categoryRepository.findById(categoryUpdate.getId())
                .orElseThrow(() ->
                        new CategoryNotFound("Category with id " + categoryUpdate.getId() + " does not exist"));

        if (!category.getUserId().equals(categoryUpdate.getUserId())) {
            throw new AccessError("You do not have access to this category");
        }

        category.setName(categoryUpdate.getName());
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    public List<CategoryDTO> findByUserId(final UUID userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }
}

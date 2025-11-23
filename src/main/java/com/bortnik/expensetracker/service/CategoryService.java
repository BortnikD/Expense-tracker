package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.category.CategoryDTO;
import com.bortnik.expensetracker.dto.category.CategoryUpdateDTO;
import com.bortnik.expensetracker.dto.category.CategoryCreateDTO;
import com.bortnik.expensetracker.entities.Category;
import com.bortnik.expensetracker.exceptions.category.CategoryAlreadyExists;
import com.bortnik.expensetracker.exceptions.category.CategoryNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.util.mappers.CategoryMapper;
import com.bortnik.expensetracker.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDTO saveCategory(CategoryCreateDTO createCategoryDTO) {
        if (categoryRepository.existsByUserIdAndName(createCategoryDTO.getUserId(), createCategoryDTO.getName())) {
            throw new CategoryAlreadyExists("Category with name " + createCategoryDTO.getName() +
                    " and userId " + createCategoryDTO.getUserId() + " already exists");
        }
        return CategoryMapper.toDto(
                categoryRepository.save(CategoryMapper.toEntity(createCategoryDTO))
        );
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryUpdateDTO categoryUpdate) {
        Category category = categoryRepository.findById(categoryUpdate.getId())
                .orElseThrow(() ->
                        new CategoryNotFound("Category with id " + categoryUpdate.getId() + " does not exist"));

        if (!category.getUserId().equals(categoryUpdate.getUserId())) {
            throw new AccessError("You do not have access to this category");
        }

        category.setName(categoryUpdate.getName());
        category.setUpdatedAt(OffsetDateTime.now());
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    public Page<CategoryDTO> getAllByUserId(final UUID userId, final Pageable pageable) {
        return categoryRepository.findByUserId(userId, pageable)
                .map(CategoryMapper::toDto);
    }

    @Cacheable(value = "categoryById", key = "#categoryId")
    public CategoryDTO getCategory(UUID categoryId) {
        return CategoryMapper.toDto(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new CategoryNotFound("Category with id " + categoryId + " does not exist")
                        )
        );
    }
}

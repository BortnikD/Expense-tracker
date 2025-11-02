package com.bortnik.expensetracker.service;

import com.bortnik.expensetracker.dto.category.CategoryDTO;
import com.bortnik.expensetracker.dto.category.CategoryUpdate;
import com.bortnik.expensetracker.dto.category.CreateCategoryDTO;
import com.bortnik.expensetracker.entities.Category;
import com.bortnik.expensetracker.exceptions.category.CategoryAlreadyExists;
import com.bortnik.expensetracker.exceptions.category.CategoryNotFound;
import com.bortnik.expensetracker.exceptions.user.AccessError;
import com.bortnik.expensetracker.repository.CategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceTests {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final CategoryService categoryService = new CategoryService(categoryRepository);

    private final UUID userId = UUID.randomUUID();
    private final UUID categoryId = UUID.randomUUID();
    private final String categoryName = "Food";
    private final String updatedName = "Groceries";

    private final CreateCategoryDTO createCategoryDTO = CreateCategoryDTO.builder()
            .userId(userId)
            .name(categoryName)
            .build();

    private final Category category = Category.builder()
            .id(categoryId)
            .userId(userId)
            .name(categoryName)
            .build();

    private final CategoryDTO categoryDTO = CategoryDTO.builder()
            .id(categoryId)
            .userId(userId)
            .name(categoryName)
            .build();

    @Test
    void saveCategory_ShouldCreateCategory() {
        when(categoryRepository.existsByUserIdAndName(userId, categoryName)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        final CategoryDTO created = categoryService.saveCategory(createCategoryDTO);

        assertEquals(categoryDTO, created);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void saveCategory_ShouldThrowException_WhenCategoryAlreadyExists() {
        when(categoryRepository.existsByUserIdAndName(userId, categoryName)).thenReturn(true);

        final var exception = assertThrows(CategoryAlreadyExists.class,
                () -> categoryService.saveCategory(createCategoryDTO));

        assertEquals("Category with name " + categoryName +
                " and userId " + userId + " already exists", exception.getMessage());
    }

    @Test
    void updateCategory_ShouldUpdateCategory() {
        final CategoryUpdate update = CategoryUpdate.builder()
                .id(categoryId)
                .userId(userId)
                .name(updatedName)
                .build();

        final Category existing = Category.builder()
                .id(categoryId)
                .userId(userId)
                .name(categoryName)
                .build();

        final Category updated = Category.builder()
                .id(categoryId)
                .userId(userId)
                .name(updatedName)
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(updated);

        final CategoryDTO result = categoryService.updateCategory(update);

        assertEquals(updatedName, result.getName());
        verify(categoryRepository).save(existing);
    }

    @Test
    void updateCategory_ShouldThrowException_WhenCategoryNotFound() {
        final CategoryUpdate update = CategoryUpdate.builder()
                .id(categoryId)
                .userId(userId)
                .name(updatedName)
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        final var exception = assertThrows(CategoryNotFound.class,
                () -> categoryService.updateCategory(update));

        assertEquals("Category with id " + categoryId + " does not exist", exception.getMessage());
    }

    @Test
    void updateCategory_ShouldThrowAccessError_WhenUserDoesNotOwnCategory() {
        final CategoryUpdate update = CategoryUpdate.builder()
                .id(categoryId)
                .userId(UUID.randomUUID())
                .name(updatedName)
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        final var exception = assertThrows(AccessError.class,
                () -> categoryService.updateCategory(update));

        assertEquals("You do not have access to this category", exception.getMessage());
    }

    @Test
    void findByUserId_ShouldReturnCategoryList() {
        when(categoryRepository.findByUserId(userId)).thenReturn(List.of(category));

        final List<CategoryDTO> result = categoryService.findByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(categoryDTO, result.getFirst());
    }

    @Test
    void findByUserId_ShouldReturnEmptyList_WhenNoCategoriesFound() {
        when(categoryRepository.findByUserId(userId)).thenReturn(List.of());

        final List<CategoryDTO> result = categoryService.findByUserId(userId);

        assertTrue(result.isEmpty());
    }
}

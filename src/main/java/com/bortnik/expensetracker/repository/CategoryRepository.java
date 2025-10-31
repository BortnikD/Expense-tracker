package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Category findByUserIdAndName(UUID userId, String name);

    List<Category> findByUserId(UUID userId);
}

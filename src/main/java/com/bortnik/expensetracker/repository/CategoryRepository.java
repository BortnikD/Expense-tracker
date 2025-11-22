package com.bortnik.expensetracker.repository;

import com.bortnik.expensetracker.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByUserIdAndName(UUID userId, String name);

    Page<Category> findByUserId(UUID userId, Pageable pageable);
}

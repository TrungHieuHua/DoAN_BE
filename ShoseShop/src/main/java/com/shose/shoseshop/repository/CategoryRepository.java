package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Category> findAllByOrderByNameAsc();

    Optional<Category> findByName(String name);
}

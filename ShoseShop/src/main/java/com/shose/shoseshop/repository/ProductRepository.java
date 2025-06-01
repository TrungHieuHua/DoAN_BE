package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategoryId(@RequestParam("id") Long categoryId);

    List<Product> findByProcedureId(@RequestParam("id") Long procedureId);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    @Query(value = "SELECT * FROM product ORDER BY created_at DESC LIMIT 4", nativeQuery = true)
    List<Product> findNewProduct();

}

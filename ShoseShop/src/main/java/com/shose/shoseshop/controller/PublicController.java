package com.shose.shoseshop.controller;


import com.shose.shoseshop.controller.response.*;
import com.shose.shoseshop.repository.CategoryRepository;
import com.shose.shoseshop.repository.ProcedureRepository;
import com.shose.shoseshop.service.CategoryService;
import com.shose.shoseshop.service.ProcedureService;
import com.shose.shoseshop.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PublicController {


    CategoryService categoryService;
    ProcedureService procedureService;
    ProductService productService;

    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> getAll() {
        List<CategoryResponse> result = categoryService.getAll1();
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Get all categories success")
                .result(result)
                .build();
    }

    @GetMapping("/procedures")
    public ApiResponse<List<ProcedureResponse>> getProcedures() {
        List<ProcedureResponse> result = procedureService.getAll();
        return ApiResponse.<List<ProcedureResponse>>builder()
                .message("Get all categories success")
                .result(result)
                .build();
    }

    @GetMapping("/categories/parent")
    public ApiResponse<List<CategoryResponse>> getParentCategories() {
        List<CategoryResponse> result = categoryService.getAll1();
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Get all parent categories success")
                .result(result)
                .build();
    }


    @GetMapping("/categories/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable("id") Long id) {
        CategoryResponse category = categoryService.getById(id);
        return ApiResponse.<CategoryResponse>builder()
                .message("Get category by id success")
                .result(category)
                .build();
    }

    @GetMapping("/products/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable("id") Long id) {
        ProductResponse product = productService.getById(id);
        return ApiResponse.<ProductResponse>builder()
                .message("Get product by id success")
                .result(product)
                .build();
    }

    @GetMapping("/products")
    public ApiResponse<Page<ProductResponse>> getAll(
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {
        Page<ProductResponse> productPage = productService.getAll(pageNum, pageSize, sortDir, sortBy);
        return ApiResponse.<Page<ProductResponse>>builder()
                .message("Get all product success")
                .result(productPage)
                .build();
    }

    @GetMapping("/products/category/{categoryId}")
    public ApiResponse<Page<ProductResponse>> getProductsByCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "30") int pageSize,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(direction, sortBy));
        Page<ProductResponse> result = productService.getByCategory(categoryId,pageable);
        return ApiResponse.<Page<ProductResponse>>builder()
                .message("Get product by category success")
                .result(result)
                .build();
    }

}

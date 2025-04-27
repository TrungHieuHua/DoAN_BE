package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.constant.Role;
import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.DataFieldProductResponse;
import com.shose.shoseshop.controller.response.ProductDetailResponse;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.convert.ProductConvert;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.repository.CategoryRepository;
import com.shose.shoseshop.repository.ProcedureRepository;
import com.shose.shoseshop.repository.ProductDetailRepository;
import com.shose.shoseshop.repository.ProductRepository;
import com.shose.shoseshop.service.ProductDetailService;
import com.shose.shoseshop.service.ProductService;
import com.shose.shoseshop.specification.ProductSpecification;
import com.shose.shoseshop.util.PaginationSortingUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProcedureRepository procedureRepository;
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;
    ProductDetailRepository productDetailRepository;
    ProductDetailService productDetailService;
    private final ProductConvert productConvert;

    @Override
    @Transactional
    public void create(ProductRequest productRequest) {
        Product product = new ModelMapper().map(productRequest, Product.class);
        Procedure procedure = procedureRepository.findById(productRequest.getProcedure())
                .orElseThrow(EntityNotFoundException::new);
        Category category = categoryRepository.findById(productRequest.getCategory())
                .orElseThrow(EntityNotFoundException::new);
        product.setProcedure(procedure);
        product.setCategory(category);
        product.setImg(productRequest.getImg());
        productRepository.save(product);
    }

    @Override
    public List<ProductResponse> getByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(product -> new ModelMapper().map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponse> getByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productsPage = productRepository.findByCategoryId(categoryId, pageable);
        return productsPage.map(product -> new ModelMapper().map(product, ProductResponse.class));
    }

//    @Override
//    public Page<DataFieldProductResponse> listProduct(Pageable pageable, ProductFilterRequest request) {
//        Specification<Product> specUser = ProductSpecification.generateFilterProducts(request);
//        Page<Product> productPage = productRepository.findAll(specUser, pageable);
//       // if (request != null && request.getRole() != null && request.getRole() == Role.USER) {
//            productPage = productPage.map(product -> {
//                product.setProductDetailResponseList(
//                        product.getProductDetailResponseList().stream().filter(productDetail -> !productDetail.getIsDeleted())
//                                .collect(Collectors.toList())
//                );
//                return product;
//            });
//       // }
//        return productPage.map(product -> modelMapper.map(product, DataFieldProductResponse.class));
//    }

    @Override
    public Page<ProductResponse> listProduct(Pageable pageable, ProductFilterRequest request) {
        Specification<Product> specUser = ProductSpecification.generateFilterProducts(request);
        Page<Product> productPage = productRepository.findAll(specUser, pageable);
        // if (request != null && request.getRole() != null && request.getRole() == Role.USER) {
        productPage = productPage.map(product -> {
            product.setProductDetailResponseList(
                    product.getProductDetailResponseList().stream().filter(productDetail -> !productDetail.getIsDeleted())
                            .collect(Collectors.toList())
            );
            return product;
        });
        // }
        return productPage.map(product -> modelMapper.map(product, ProductResponse.class));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<ProductDetail> productDetailList = product.getProductDetailResponseList();
        productDetailList.forEach(BaseEntity::markAsDelete);
        product.markAsDelete();
        productDetailRepository.saveAll(productDetailList);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void update(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId()).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(productRequest, product);
        product.setImg(productRequest.getImg());
        productRepository.save(product);
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public void updateStatus(Long id) {
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        product.setIsDeleted(false);
        productRepository.save(product);

    }

    @Override
    public Page<ProductResponse> getAll(int pageNum, int pageSize, String sortDir, String sortBy) {
        Pageable pageable = PaginationSortingUtils.getPageable(pageNum, pageSize, sortDir, sortBy);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> productConvert.convertToDTO(product));
    }

    @Override
    public Page<ProductResponse> getProductDiscount(int pageNum, int pageSize, String sortDir, String sortBy) {
        Pageable pageable = PaginationSortingUtils.getPageable(pageNum, pageSize, sortDir, sortBy);
        Page<Product> productPage = productRepository.findByDiscount(pageable);
        return productPage.map(product -> productConvert.convertToDTO(product));
    }


}
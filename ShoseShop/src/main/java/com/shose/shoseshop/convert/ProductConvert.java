package com.shose.shoseshop.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.entity.Category;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.exception.AppException;
import com.shose.shoseshop.exception.ErrorResponse;
import com.shose.shoseshop.repository.CategoryRepository;
import com.shose.shoseshop.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductConvert {

    ModelMapper modelMapper;
    ProductRepository productRepository;
    ObjectMapper objectMapper;
    CategoryRepository categoryRepository;


    public Product convertToEntity(ProductRequest request)
    {
        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(()-> new AppException(ErrorResponse.CATEGORY_NOT_EXISTED));
        Product product  = modelMapper.map(request, Product.class);
        product.setCategory(category);
        return  product;
    }

    public ProductResponse convertToDTO(Product product)
    {
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        return response;
    }
}

package com.shose.shoseshop.controller.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DataFieldProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal priceRange;
    private Float star;
    private String img;
}

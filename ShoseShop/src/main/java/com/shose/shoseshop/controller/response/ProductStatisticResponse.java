package com.shose.shoseshop.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatisticResponse {
    private Long productId;
    private String productName;
    private Long totalQuantitySold;
}

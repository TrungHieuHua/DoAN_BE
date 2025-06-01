package com.shose.shoseshop.kmeans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {

    private Long id;
    private int age;
    private BigDecimal totalSpending;
    private Double purchaseCount;
    private int recency;
}

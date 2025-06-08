package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.kmeans.CustomerConvert;
import com.shose.shoseshop.kmeans.CustomerDTO;
import com.shose.shoseshop.kmeans.KMeanService;
import com.shose.shoseshop.kmeans.UserClusterService;
import com.shose.shoseshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/kmeans")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class KMeansController {
    KMeanService kmeanService;
    UserClusterService userClusterService;

    @GetMapping("/segment")
    public ResponseData<?> segmentCustomers() {
        Map<Integer, List<CustomerDTO>> segment = kmeanService.genCluster();
        return new ResponseData<>(segment);
    }

    @GetMapping()
    public ResponseData<?> getProductCluster() {
        List<ProductResponse> list = userClusterService.getProductCluster();
        return new ResponseData<>(list);
    }
}

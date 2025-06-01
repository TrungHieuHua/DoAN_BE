package com.shose.shoseshop.kmeans;

import com.shose.shoseshop.controller.response.ProductDetailResponse;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.ProductDetail;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.entity.UserCluster;
import com.shose.shoseshop.repository.UserClusterRepository;
import com.shose.shoseshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shose.shoseshop.entity.UserCluster_.userId;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserClusterService {

    UserClusterRepository userClusterRepository;
    UserRepository userRepository;

    public void saveUserClusters(Map<Integer, List<CustomerDTO>> segments) {
        List<UserCluster> clusters = new ArrayList<>();

        for (Map.Entry<Integer, List<CustomerDTO>> entry : segments.entrySet()) {
            Integer clusterGroup = entry.getKey();
            for (CustomerDTO dto : entry.getValue()) {
                Optional<UserCluster> optional = userClusterRepository.findByUserId(dto.getId());
                UserCluster userCluster = optional.orElse(new UserCluster());
                userCluster.setUserId(dto.getId());
                userCluster.setClusterGroup(clusterGroup);
                clusters.add(userCluster);
            }
        }

        userClusterRepository.saveAll(clusters);
    }

    public List<ProductResponse> getProductCluster() {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       //log.info("login : {}",loginUser);;
        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
        List<Product> products = userClusterRepository.findProductsPurchasedByClusterGroup(user.getId());
        return products.stream()
                .map(product -> new ModelMapper().map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }
}


package com.shose.shoseshop.kmeans;

import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import smile.clustering.KMeans;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class KMeanService {

    UserRepository userRepository;
    UserClusterService userClusterService;
    CustomerConvert customerCovert;

    public double[][] convertToMatrix(List<CustomerDTO> customers) {
        double[][] data = new double[customers.size()][4];
        for (int i = 0; i < customers.size(); i++) {
            CustomerDTO c = customers.get(i);
            data[i][0] = c.getAge();
            data[i][1] = c.getTotalSpending().doubleValue();
            data[i][2] = c.getPurchaseCount();
            data[i][3] = c.getRecency();
        }
        return data;
    }

    public Map<Integer, List<CustomerDTO>> clusterCustomers(List<CustomerDTO> customers, int k) {
        double[][] data = convertToMatrix(customers);
        KMeans kmeans = KMeans.fit(data, k);
        int[] labels = kmeans.y;

        Map<Integer, List<CustomerDTO>> result = new HashMap<>();
        for (int i = 0; i < labels.length; i++) {
            result.computeIfAbsent(labels[i], x -> new ArrayList<>()).add(customers.get(i));
        }

        return result;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public Map<Integer, List<CustomerDTO>> genCluster() {
        List<User> list = userRepository.findAll()
                .stream()
                .filter(user -> user.getId() != 1)
                .toList();
        List<CustomerDTO> customerDTOS = list.stream()
                .map((user -> {
                    try {
                        return customerCovert.convert(user);
                    } catch (Exception e) {
                        log.info("Convert failed for user: " + user.getId());
                        e.printStackTrace();
                        return null;
                    }
                }))
                .filter(Objects::nonNull)
                .toList();
        if (customerDTOS.isEmpty()) {
            log.info("customerDTOS error: ");
        }
        Map<Integer, List<CustomerDTO>> segments = clusterCustomers(customerDTOS, 3);
        userClusterService.saveUserClusters(segments);
        return segments;
    }
}

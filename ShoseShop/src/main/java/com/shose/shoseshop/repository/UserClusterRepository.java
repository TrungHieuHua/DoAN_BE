package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.UserCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserClusterRepository extends JpaRepository<UserCluster, Long> {

    Optional<UserCluster> findByUserId(Long userId);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.productDetailResponseList pd " +
            "JOIN OrderDetail od ON od.productDetail = pd " +
            "JOIN Order o ON od.order = o " +
            "JOIN UserCluster uc ON o.user.id = uc.userId " +
            "WHERE uc.clusterGroup = (" +
            "  SELECT u2.clusterGroup FROM UserCluster u2 WHERE u2.userId = :userId" +
            ")")
    List<Product> findProductsPurchasedByClusterGroup(@Param("userId") Long userId);
}

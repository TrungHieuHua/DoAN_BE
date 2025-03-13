package com.shose.shoseshop.specification;

import com.shose.shoseshop.constant.Role;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.entity.Voucher;
import com.shose.shoseshop.entity.Voucher_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class VoucherSpecification {

    private static Specification<Voucher> hasDateFrom(Date dateFrom) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(dateFrom.toString())) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get(Voucher_.CREATED_AT), dateFrom);
        };
    }

    private static Specification<Voucher> hasDateTo(Date dateTo) {
        return (root, query, cb) ->{
            if (StringUtils.isEmpty(dateTo.toString())) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get(Voucher_.CREATED_AT), dateTo);
        };
    }

    private static Specification<Voucher> isDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get(Product_.IS_DELETED));

    }

        public static Specification<Voucher> generateFilter(OrderFilterRequest request) {
                return hasDateFrom(request.getDateFrom()).
                        and(hasDateTo(request.getDateTo())).
                        and(isDeleted());
    }
}

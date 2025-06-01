package com.shose.shoseshop.kmeans;

import com.shose.shoseshop.constant.PaymentStatus;
import com.shose.shoseshop.entity.Order;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.OrderRepository;
import com.shose.shoseshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CustomerConvert {

    UserRepository userRepository;
    OrderRepository orderRepository;

    public CustomerDTO convert(User user) {
        Long id = user.getId();

        CustomerDTO dto = new CustomerDTO();
        List<Order> listOrder = orderRepository.findByUser_Id(id);

        BigDecimal totalSpending = listOrder.stream()
                .filter(order -> order.getPaymentStatus() == PaymentStatus.COMPLETE)
                .map(Order::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Double purchaseCount = orderRepository.countByUser_Id(id);
        int age = calculateAge(user.getBirthday());

        dto.setId(user.getId());
        dto.setAge(age);
        dto.setTotalSpending(totalSpending);
        dto.setPurchaseCount(purchaseCount);

        Order lastOrder = orderRepository.findTopByUser_IdOrderByCreatedAtDesc(id);
        if (lastOrder != null && lastOrder.getCreatedAt() != null) {
            Date lastPurchaseDate = Date.from(lastOrder.getCreatedAt());
            dto.setRecency(calculateDay(lastPurchaseDate));
        } else {
            dto.setRecency(Integer.MAX_VALUE); // hoặc 0
        }

        return dto;
    }

    public CustomerDTO convert2(Long id) {
        System.out.println("⏳ [convert] Bắt đầu convert userId = " + id);

        CustomerDTO dto = new CustomerDTO();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ User not found: " + id));

        System.out.println("✅ User tìm thấy: " + user.getId());

        List<Order> listOrder = orderRepository.findByUser_Id(id);
        System.out.println("📦 Tổng số đơn hàng: " + listOrder.size());

        BigDecimal totalSpending = listOrder.stream()
                .filter(order -> order.getPaymentStatus() == PaymentStatus.COMPLETE)
                .map(Order::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("💰 Tổng chi tiêu: " + totalSpending);

        Double purchaseCount = orderRepository.countByUser_Id(id);
        System.out.println("🛍️ Số lần mua hàng: " + purchaseCount);

        int age = calculateAge(user.getBirthday());
        System.out.println("🎂 Tuổi: " + age);

        Order lastOrder = orderRepository.findTopByUser_IdOrderByCreatedAtDesc(id);
        if (lastOrder != null && lastOrder.getCreatedAt() != null) {
            Date lastPurchaseDate = Date.from(lastOrder.getLastModifiedAt());
            int recency = calculateDay(lastPurchaseDate);
            System.out.println("📅 Số ngày từ lần mua gần nhất: " + recency);
            dto.setRecency(recency);
        } else {
            System.out.println("⚠️ Không có đơn hàng gần nhất");
            dto.setRecency(Integer.MAX_VALUE);
        }

        dto.setId(user.getId());
        dto.setAge(age);
        dto.setTotalSpending(totalSpending);
        dto.setPurchaseCount(purchaseCount);

        System.out.println("✅ [convert] Hoàn tất convert userId = " + id);
        return dto;
    }

    public CustomerDTO convert1(Long id) {
        CustomerDTO dto = new CustomerDTO();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> listOrder = orderRepository.findByUser_Id(id);
        BigDecimal totalSpending = listOrder.stream().filter(order -> order.getPaymentStatus() == PaymentStatus.COMPLETE)
                .map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        Double purchaseCount = orderRepository.countByUser_Id(id);
        int age = calculateAge(user.getBirthday());
        dto.setId(user.getId());
        dto.setAge(age);
        dto.setTotalSpending(totalSpending);
        dto.setPurchaseCount(purchaseCount);
        Date lastPurchaseDate = Date.from(orderRepository.findTopByUser_IdOrderByCreatedAtDesc(id).getCreatedAt());
        dto.setRecency(calculateDay(lastPurchaseDate));


        return dto;
    }

    public int calculateAge(Date birthday) {
        if (birthday == null) return 0;

        LocalDate birthDate;

        if (birthday instanceof java.sql.Date) {
            birthDate = ((java.sql.Date) birthday).toLocalDate(); // ✅ tránh toInstant()
        } else {
            birthDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public int calculateDay(Date day) {
        if (day == null) return 0;
        LocalDate date = day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        return Period.between(date, now).getDays();
    }
}

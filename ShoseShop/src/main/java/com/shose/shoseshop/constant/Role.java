package com.shose.shoseshop.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN,
    USER;

    @JsonCreator
    public static Role fromString(String value) {
        try {
            return Role.valueOf(value.toUpperCase()); // Chuyển về chữ hoa để tránh lỗi
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + value);
        }
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase(); // Trả về chữ thường khi serialize
    }
}

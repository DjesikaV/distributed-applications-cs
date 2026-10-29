package com.djesikav.motostock.dto.response;

import com.djesikav.motostock.entity.User;
import com.djesikav.motostock.enums.Role;

import java.time.LocalDateTime;

public record UserResponse (
        Long id,
        String username,
        String email,
        Role role,
        Boolean enabled,
        LocalDateTime createdAt
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.getCreateAt()
        );
    }
}
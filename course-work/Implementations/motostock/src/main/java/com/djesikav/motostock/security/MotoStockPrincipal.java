package com.djesikav.motostock.security;

import com.djesikav.motostock.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public final class MotoStockPrincipal
        extends org.springframework.security.core.userdetails.User {

    private final Long userId;

    public MotoStockPrincipal(User user) {
        super(
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );

        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
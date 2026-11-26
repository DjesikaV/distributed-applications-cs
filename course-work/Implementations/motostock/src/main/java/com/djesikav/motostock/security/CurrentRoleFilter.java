package com.djesikav.motostock.security;

import com.djesikav.motostock.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

final class CurrentRoleFilter extends OncePerRequestFilter {

    private final UserRepository users;
    private final SecurityContextLogoutHandler logout = new SecurityContextLogoutHandler();

    CurrentRoleFilter(UserRepository users) {
        this.users = users;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            boolean current = auth.getPrincipal() instanceof MotoStockPrincipal principal
                    && users.findById(principal.getUserId())
                    .filter(user -> user.getUsername().equals(principal.getUsername()))
                    .filter(user -> user.isEnabled() && auth.getAuthorities().contains(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                    .isPresent();

            if (!current) {
                logout.logout(request, response, auth);
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}

package com.djesikav.motostock.controller;

import com.djesikav.motostock.dto.response.UserResponse;
import com.djesikav.motostock.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                Math.min(Math.max(1, size), 50),
                Sort.by("username").ascending()
        );

        Page<UserResponse> users = userService.findAll(pageable)
                .map(UserResponse::fromEntity);

        model.addAttribute("users", users.getContent());
        model.addAttribute("userPage", users);

        return "admin/users/list";
    }
}

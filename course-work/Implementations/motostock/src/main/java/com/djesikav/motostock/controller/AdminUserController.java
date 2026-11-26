package com.djesikav.motostock.controller;

import com.djesikav.motostock.dto.form.AdminCreateUserForm;
import com.djesikav.motostock.dto.response.UserResponse;
import com.djesikav.motostock.enums.Role;
import com.djesikav.motostock.service.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("roles")
    public Role[] roles() {
        return Role.values();
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

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        AdminCreateUserForm form = new AdminCreateUserForm();
        form.setRole(Role.USER);

        model.addAttribute("userForm", form);

        return "admin/users/create";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("userForm") AdminCreateUserForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/users/create";
        }

        if (form.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            bindingResult.rejectValue(
                    "password",
                    "password.tooLong",
                    "Password is too long. Please use fewer characters."
            );

            return "admin/users/create";
        }

        try {
            userService.createByAdmin(form);
        } catch (DataIntegrityViolationException ex) {
            bindingResult.reject(
                    "userCreationError",
                    "Could not create the user. The username or email may already be in use."
            );

            return "admin/users/create";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "User created successfully."
        );

        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/role")
    public String changeRole(
            @PathVariable Long id,
            @RequestParam Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            RedirectAttributes redirectAttributes) {

        try {
            var user = userService.changeRole(id, role);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    user.getUsername() + " now has the " + user.getRole() + " role."
            );
        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This account no longer exists."
            );
        }

        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);

        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/enabled")
    public String setEnabled(
            @PathVariable Long id,
            @RequestParam boolean enabled,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            RedirectAttributes redirectAttributes) {

        try {
            var user = userService.setEnabled(id, enabled);
            String status = user.isEnabled() ? "enabled" : "disabled";

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    user.getUsername() + " is now " + status + "."
            );
        } catch (EntityNotFoundException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This account no longer exists."
            );
        }

        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);

        return "redirect:/admin/users";
    }
}
package com.djesikav.motostock.controller;

import com.djesikav.motostock.dto.form.RegisterForm;
import com.djesikav.motostock.service.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {

        model.addAttribute("registerForm", new RegisterForm());

        return "register";
    }

    @PostMapping
    public String register(
            @Valid @ModelAttribute("registerForm") RegisterForm form,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (form.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            bindingResult.rejectValue(
                    "password",
                    "password.tooLong",
                    "Password cannot exceed 72 UTF-8 bytes."
            );
            return "register";
        }

        try {
            userService.register(form);
        } catch (DataIntegrityViolationException ex) {
            bindingResult.reject(
                    "registrationError",
                    "Username or email is already registered."
            );
            return "register";
        }

        return "redirect:/login?registered";
    }
}

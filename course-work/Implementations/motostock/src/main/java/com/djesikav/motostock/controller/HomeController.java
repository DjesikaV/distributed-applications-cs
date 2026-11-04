package com.djesikav.motostock.controller;

import org.springframework.ui.Model;import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Principal principal) {

        String username = principal != null
                ? principal.getName()
                : "Guest";

        model.addAttribute("username", username);

        return "index";
    }
}

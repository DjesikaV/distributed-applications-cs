package com.djesikav.motostock.security;

import com.djesikav.motostock.entity.User;
import com.djesikav.motostock.enums.Role;
import com.djesikav.motostock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Component
public class AdminBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${MOTOSTOCK_BOOTSTRAP_ADMIN:false}")
    private boolean bootstrapEnabled;

    @Value("${MOTOSTOCK_ADMIN_USERNAME:}")
    private String username;

    @Value("${MOTOSTOCK_ADMIN_EMAIL:}")
    private String email;

    @Value("${MOTOSTOCK_ADMIN_PASSWORD:}")
    private String password;


    public AdminBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!bootstrapEnabled) {
            return;
        }

        if (userRepository.count() != 0) {
            throw new IllegalStateException("Admin bootstrap requires an empty users table. Disable bootstrap.");
        }

        if (username.isBlank() || username.length() < 3 || username.length() > 50) {
            throw new IllegalStateException("Username must be between 3 and 50 characters.");
        }

        if (email.isBlank() || email.length() > 120 || !email.contains("@")) {
            throw new IllegalStateException("Email is invalid.");
        }

        int passwordBytes = password.getBytes(StandardCharsets.UTF_8).length;

        if (password.isBlank() || password.length() < 12 || passwordBytes > 72) {
            throw new IllegalStateException("Password must be between 1 and 72 characters.");
        }

        User admin = new User();

        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);

        userRepository.save(admin);

        System.out.println("Admin has been created.");
    }

}

package com.djesikav.motostock.service;

import com.djesikav.motostock.entity.User;
import com.djesikav.motostock.enums.Role;
import com.djesikav.motostock.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User register(User input, String rawPassword) {
        return saveNewUser(input, rawPassword, Role.USER);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User createByAdmin(User input, String rawPassword, Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }

        return saveNewUser(input, rawPassword, role);
    }

    private User saveNewUser(User input, String rawPassword, Role role) {
        if (input == null) {
            throw new IllegalArgumentException("User data is required");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = new User();

        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User changeRole(Long id, Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }

        User user = findById(id);
        user.setRole(role);

        return userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User update(Long id, User updatedUser) {
        User user = findById(id);

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());

        return userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void changeOwnPassword(String currentPassword, String newPassword) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User setEnabled(Long id, boolean enabled) {
        User user = findById(id);

        user.setEnabled(enabled);

        return userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void resetPasswordByAdmin(Long id, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        User user  = findById(id);

        user.setPasswordHash(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void deleteOwnAccount(String currentPassword) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalStateException("You are not allowed to perform this operation");
        }

        userRepository.delete(user);
    }
}

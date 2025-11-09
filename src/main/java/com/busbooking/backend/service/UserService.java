package com.busbooking.backend.service;

import com.busbooking.backend.dto.RegisterRequest;
import com.busbooking.backend.dto.RegisterResponse;
import com.busbooking.backend.entity.User;
import com.busbooking.backend.exception.EmailAlreadyExistsException;
import com.busbooking.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already exists - {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already registered: " + request.getEmail());
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create user entity
        User user = User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .build();

        // Save user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Return response
        return RegisterResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .message("User registered successfully")
                .build();
    }
}
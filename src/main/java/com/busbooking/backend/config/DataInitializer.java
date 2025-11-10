package com.busbooking.backend.config;

import com.busbooking.backend.model.Role;
import com.busbooking.backend.model.User;
import com.busbooking.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User testUser = User.builder()
                    .email("test@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .firstName("Test")
                    .lastName("User")
                    .role(Role.USER)
                    .build();

            userRepository.save(testUser);
            System.out.println("Test user created: test@example.com / password123");
        }
    }
}
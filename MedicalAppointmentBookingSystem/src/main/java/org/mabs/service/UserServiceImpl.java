package org.mabs.service;

import org.mabs.dto.UserRegistrationDto;
import org.mabs.entity.User;
import org.mabs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserRegistrationDto dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());

        // Encrypt password
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // Default role and status
        user.setRole("patient");
        user.setStatus("active");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}

package org.mabs.service.impl;

import org.mabs.dto.UserProfileUpdateDto;
import org.mabs.dto.UserRegistrationDto;
import org.mabs.entity.User;
import org.mabs.exception.DuplicateEmailException;
import org.mabs.repository.UserRepository;
import org.mabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getRoleDoctor() {
        return userRepository.findByRole("doctor");
    }

    @Override
    public User addUser(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException(user.getEmail());
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User existing = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản cần cập nhật!"));

        // Kiểm tra trùng email với các tài khoản khác
        User userWithSameEmail = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(user.getId())) {
            throw new DuplicateEmailException(user.getEmail());
        }

        // Chỉ mã hóa nếu mật khẩu mới được truyền vào và khác với mật khẩu cũ trong DB
        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
            if (!user.getPasswordHash().equals(existing.getPasswordHash())) {
                user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }
        } else {
            // Giữ lại mật khẩu cũ đã mã hóa
            user.setPasswordHash(existing.getPasswordHash());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void saveUser(UserRegistrationDto dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        if (!existsByEmail(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        } else {
            throw new DuplicateEmailException("Email đã tồn tại: " + dto.getEmail());
        }

        // Encrypt password
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // Default role and status
        user.setRole("patient");
        user.setStatus("active");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void updateProfile(String email, UserProfileUpdateDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAddress(dto.getAddress());

        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }
}

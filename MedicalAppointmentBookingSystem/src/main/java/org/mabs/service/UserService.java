package org.mabs.service;

import org.mabs.dto.UserProfileUpdateDto;
import org.mabs.dto.UserRegistrationDto;
import org.mabs.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    List<User> getRoleDoctor();

    List<User> getRoleDoctorWithNoProfile();

    User addUser(User user);

    User updateUser(User user, String newPassword);

    void deleteUser(Long id);

    User findById(Long id);

    boolean existsByEmail(String email);

//    String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto);
    void saveUser(UserRegistrationDto registrationDto);

    void updateProfile(String email, UserProfileUpdateDto dto);

    User getUserByEmail(String email);
}

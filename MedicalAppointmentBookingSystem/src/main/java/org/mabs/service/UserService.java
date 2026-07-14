package org.mabs.service;

import org.mabs.dto.UserProfileUpdateDto;
import org.mabs.dto.UserRegistrationDto;
import org.mabs.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    List<User> getRoleDoctor();

    User addUser(User user);

    User updateUser(User user);

    User findById(Long id);

//    String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto);
    void saveUser(UserRegistrationDto registrationDto);

    void updateProfile(String email, UserProfileUpdateDto dto);

    User getUserByEmail(String email);
}

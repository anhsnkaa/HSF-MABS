package org.mabs.service;

import org.mabs.dto.UserProfileUpdateDto;
import org.mabs.dto.UserRegistrationDto;
import org.mabs.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public interface UserService {
//    String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto);
    void saveUser(UserRegistrationDto registrationDto);

    void updateProfile(String email, UserProfileUpdateDto dto);

    User getUserByEmail(String email);
}

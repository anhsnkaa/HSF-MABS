package org.mabs.service;

import org.mabs.dto.UserRegistrationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public interface UserService {
//    String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto);
    void saveUser(UserRegistrationDto registrationDto);
}

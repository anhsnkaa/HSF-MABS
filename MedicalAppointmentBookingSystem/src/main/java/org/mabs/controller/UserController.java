package org.mabs.controller;

import org.mabs.dto.UserProfileUpdateDto;
import org.mabs.entity.User;
import org.mabs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model) {
        //Principal(Spring Security)
        String email = principal.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setDob(user.getDateOfBirth());
        dto.setAddress(user.getAddress());

        model.addAttribute("userProfile", dto);
        return "profile";
    }
}

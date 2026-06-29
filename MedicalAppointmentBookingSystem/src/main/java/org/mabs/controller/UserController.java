package org.mabs.controller;

import jakarta.validation.Valid;
import org.mabs.dto.UserProfileUpdateDto;
import org.mabs.entity.User;
import org.mabs.repository.UserRepository;
import org.mabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model) {
        //Not login yet -> req login
        if (principal == null) {
            return "redirect:/login";
        }

        //Principal(Spring Security)
        String email = principal.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());

        model.addAttribute("userProfile", dto);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("userProfile") UserProfileUpdateDto dto,
                                BindingResult result,
                                Principal principal) {
        if (result.hasErrors()) {
            return "profile";
        }

        userService.updateProfile(principal.getName(), dto);

        return "redirect:/profile?success"; //flag success -> display notification to UI
    }
}

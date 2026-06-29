package org.mabs.controller;

import jakarta.validation.Valid;
import org.mabs.dto.UserRegistrationDto; // DTO bạn tạo
import org.mabs.service.UserService;     // Bạn nên tạo Service để xử lý logic lưu user
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private UserService userService; // (Encrypted password)

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserRegistrationDto registrationDto,
                                      BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.saveUser(registrationDto);
        return "redirect:/login?success";
    }
}

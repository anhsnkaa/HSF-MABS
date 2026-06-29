package org.mabs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.AccountCreationDto;
import org.mabs.entity.User;
import org.mabs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final UserService userService;

    @GetMapping
    public String viewAllAccounts(Model model) {
        model.addAttribute("accountList", userService.getAllUsers());
        return "admin/account/account-list";
    }

    @GetMapping("/add")
    public String addAccount(Model model) {
        model.addAttribute("dto", new AccountCreationDto());
        return "/admin/account/add-account";
    }

    @PostMapping("/add")
    public String addAccount(@Valid @ModelAttribute("dto") AccountCreationDto dto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/admin/account/add-account";
        }
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPasswordHash(dto.getPassword());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAddress(dto.getAddress());
        user.setAvatarUrl(dto.getAvatarUrl());

        userService.addUser(user);
        redirectAttributes.addFlashAttribute("message", "Added successfully!");
        return "redirect:/accounts";
    }
}

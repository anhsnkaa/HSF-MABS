package org.mabs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.AccountCreationDto;
import org.mabs.dto.AccountUpdateDto;
import org.mabs.entity.User;
import org.mabs.exception.DuplicateEmailException;
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
    public String addAccount(@Valid @ModelAttribute("dto") AccountUpdateDto dto,
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

        try {
            userService.addUser(user);
            redirectAttributes.addFlashAttribute("message", "Added successfully!");
            return "redirect:/accounts";
        } catch (DuplicateEmailException e) {
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            return "/admin/account/add-account";
        }

    }

    @PostMapping("/update-form")
    public String updateAccountForm(@RequestParam(name = "id") Long id,
                                Model model) {
        User user = userService.findById(id);
        AccountUpdateDto dto = new AccountUpdateDto();
        dto.setId(id);
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setGender(user.getGender());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());
        dto.setAvatarUrl(user.getAvatarUrl());
        model.addAttribute("dto", dto);
        return "/admin/account/account-update";
    }

    @PostMapping("/update")
    public String updateAccount(@Valid @ModelAttribute(name = "dto") AccountUpdateDto dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/admin/account/account-update";
        }

        User user = userService.findById(dto.getId());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPasswordHash(dto.getPassword());
        }
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());
        user.setGender(dto.getGender());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAddress(dto.getAddress());
        user.setAvatarUrl(dto.getAvatarUrl());

        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("message", "Updated successfully!");
            return "redirect:/accounts";
        } catch (DuplicateEmailException e) {
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            return "/admin/account/account-update";
        }


    }
}

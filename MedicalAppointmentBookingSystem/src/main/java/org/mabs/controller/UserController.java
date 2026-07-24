package org.mabs.controller;

import jakarta.validation.Valid;
import org.mabs.dto.UserProfileUpdateDto;
import org.mabs.entity.User;
import org.mabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class UserController {

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

        User user = userService.getUserByEmail(email);

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
                                Principal principal,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "profile";
        }

        try{
            userService.updateProfile(principal.getName(), dto);

            // Lấy role để redirect trực tiếp, tránh bị double redirect làm mất successMessage
            User user = userService.getUserByEmail(principal.getName());
            String role = user.getRole().toLowerCase();

            //Notification: "Update succcess"
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");

            switch (role) {
                case "admin":
                    return "redirect:/admin/dashboard";
                case "doctor":
                    return "redirect:/doctors/dashboard";
                default:
                    return "redirect:/patient/dashboard";
            }
        } catch (Exception e){
            //Notification: "Update failed"
            model.addAttribute("error", "Có lỗi xảy ra, vui lòng thử lại sau!");
            return "profile";
        }
    }
}


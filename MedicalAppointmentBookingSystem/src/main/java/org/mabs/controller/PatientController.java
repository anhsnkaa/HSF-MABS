package org.mabs.controller;

import org.mabs.entity.User;
import org.mabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String patientDashboard(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        // Logic check thông tin cá nhân
        boolean isProfileComplete = (user.getPhone() != null && !user.getPhone().isEmpty())
                && user.getDateOfBirth() != null;

        model.addAttribute("user", user);
        model.addAttribute("isProfileComplete", isProfileComplete);

        // Patient-page
        return "patient-dashboard";
    }
}

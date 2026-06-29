package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.entity.User;
import org.mabs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String doctorDashboard(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);

        return "doctor-dashboard";
    }
}

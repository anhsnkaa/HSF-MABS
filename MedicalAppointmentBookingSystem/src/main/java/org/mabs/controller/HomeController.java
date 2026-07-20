package org.mabs.controller;

import org.mabs.entity.User;
import org.mabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/home/admin")
    public String homeAdmin() {
        return "admin/admin-home";
    }

    @GetMapping("/about")
    public String about() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String dashboard(Principal principal) {
        // 1. Nếu chưa đăng nhập -> login
        if (principal == null) {
            return "redirect:/login";
        }

        // 1. Lấy user từ DB lên để check Role thật
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        String role = user.getRole().toLowerCase();

        // 2. Redirect theo role
        switch (role) {
            case "admin":
                return "redirect:/admin/dashboard";
            case "doctor":
                return "redirect:/doctors/schedule";
            case "patient":
                return "redirect:/patient/dashboard";

            default:
                return "redirect:/login?error";
        }
    }
}

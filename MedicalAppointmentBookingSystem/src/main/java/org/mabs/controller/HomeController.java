package org.mabs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
//         model.addAttribute("specialties", specialtyService.findAll());
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}

package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    private String getAllDoctors(Model model) {
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        return "/admin/admin-doctor";
    }
}

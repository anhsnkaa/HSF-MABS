package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.entity.Specialty;
import org.mabs.entity.User;
import org.mabs.repository.SpecialtyRepository;
import org.mabs.service.DoctorService;
import org.mabs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final SpecialtyRepository specialtyRepository;

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

    @GetMapping("/doctors")
    public String listDoctors(@RequestParam DoctorSearch doctorSearch, Model model) {
        List<Specialty> specialties = specialtyRepository.findAll();
        List<Doctor> doctors = doctorService.searchDoctors(doctorSearch);

        model.addAttribute("specialties", specialties);
        model.addAttribute("doctors", doctors);
        model.addAttribute("doctorSearch", doctorSearch);
        return "doctors";
    }
}

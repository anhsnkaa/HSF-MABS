package org.mabs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorCreationDto;
import org.mabs.entity.Doctor;
import org.mabs.entity.User;
import org.mabs.service.DoctorService;
import org.mabs.service.SpecialtyService;
import org.mabs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final UserService userService;
    private final SpecialtyService specialtyService;

    @GetMapping
    private String getAllDoctors(Model model) {
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        return "/admin/doctor/doctor-list";
    }

    @GetMapping("/add")
    private String addDoctor(Model model) {
        model.addAttribute("dto", new DoctorCreationDto());
        model.addAttribute("doctorRoleList", userService.getRoleDoctor());
        model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
        return "/admin/doctor/doctor-add";
    }

    @PostMapping("/add")
    private String addDoctor(@Valid @ModelAttribute("dto") DoctorCreationDto dto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctorRoleList", userService.getRoleDoctor());
            model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
            return "/admin/doctor/doctor-add";
        }

        Doctor doctor = new Doctor();
        doctor.setUser(userService.findById(dto.getUserId()));
        doctor.setSpecialty(specialtyService.findById(dto.getSpecialtyId()));
        doctor.setTitle(dto.getTitle());
        doctor.setBio(dto.getBio());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setExperienceYears(dto.getExperienceYears());

        doctorService.createDoctor(doctor);
        redirectAttributes.addFlashAttribute("message", "Added successfully");
        return "redirect:/doctors";
    }
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

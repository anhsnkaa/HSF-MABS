package org.mabs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorCreationDto;
import org.mabs.dto.DoctorUpdateDto;
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
    public String getAllDoctors(Model model) {
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        return "/admin/doctor/doctor-list";
    }

    @GetMapping("/add")
    public String addDoctor(Model model) {
        model.addAttribute("dto", new DoctorCreationDto());
        model.addAttribute("doctorRoleList", userService.getRoleDoctor());
        model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
        return "/admin/doctor/doctor-add";
    }

    @PostMapping("/add")
    public String addDoctor(@Valid @ModelAttribute("dto") DoctorCreationDto dto,
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

    @PostMapping("/update-form")
    public String updateDoctorForm(@RequestParam(name = "id") Long id,
                               Model model) {
        Doctor doctor = doctorService.findById(id);
        DoctorUpdateDto dto = new DoctorUpdateDto();
        dto.setId(doctor.getId());
        dto.setUserId(doctor.getUser().getId());
        dto.setSpecialtyId(doctor.getSpecialty().getId());
        dto.setTitle(doctor.getTitle());
        dto.setBio(doctor.getBio());
        dto.setConsultationFee(doctor.getConsultationFee());
        dto.setExperienceYears(doctor.getExperienceYears());

        model.addAttribute("dto", dto);
        model.addAttribute("doctorRoleList", userService.getRoleDoctor());
        model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
        return "/admin/doctor/doctor-update";
    }

    @PostMapping("/update")
    public String updateDoctor(@Valid @ModelAttribute(name = "dto") DoctorUpdateDto dto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctorRoleList", userService.getRoleDoctor());
            model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
            return "/admin/doctor/doctor-update";
        }

        Doctor doctor = doctorService.findById(dto.getId());
        doctor.setUser(userService.findById(dto.getUserId()));
        doctor.setSpecialty(specialtyService.findById(dto.getSpecialtyId()));
        doctor.setTitle(dto.getTitle());
        doctor.setBio(dto.getBio());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setExperienceYears(dto.getExperienceYears());

        doctorService.updateDoctor(doctor);
        redirectAttributes.addFlashAttribute("message", "Updated successfully");
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

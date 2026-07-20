package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.entity.Specialty;
import org.mabs.entity.User;
import org.mabs.service.DoctorService;
import org.mabs.service.SpecialtyService;
import org.mabs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;

    @GetMapping("/dashboard")
    public String patientDashboard(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        boolean isProfileComplete = (user.getPhone() != null && !user.getPhone().isEmpty())
                && user.getDateOfBirth() != null;

        List<Specialty> specialties = specialtyService.getALlSpecialties();

        model.addAttribute("user", user);
        model.addAttribute("isProfileComplete", isProfileComplete);
        model.addAttribute("specialties", specialties);

        return "patient/patient-dashboard";
    }

    @GetMapping("/doctors")
    public String listDoctors(DoctorSearch doctorSearch, Model model) {
        List<Specialty> specialties = specialtyService.getALlSpecialties();
        List<Doctor> doctors = doctorService.searchDoctors(doctorSearch);

        model.addAttribute("specialties", specialties);
        model.addAttribute("doctors", doctors);
        model.addAttribute("criteria", doctorSearch);
        return "patient/doctors";
    }

    @GetMapping("/doctors/{id}")
    public String doctorDetail(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "patient/doctor-detail";
    }
}

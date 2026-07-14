package org.mabs.controller;

import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.entity.Specialty;
import org.mabs.entity.User;
import org.mabs.repository.AppointmentRepository;
import org.mabs.service.DoctorService;
import org.mabs.service.SpecialtyService;
import org.mabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SpecialtyService specialtyService;

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

        return "patient-dashboard";
    }

    @GetMapping("/doctors")
    public String listDoctors(DoctorSearch doctorSearch, Model model) {
        List<Specialty> specialties = specialtyService.getALlSpecialties();
        List<Doctor> doctors = doctorService.searchDoctors(doctorSearch);

        model.addAttribute("specialties", specialties);
        model.addAttribute("doctors", doctors);
        model.addAttribute("doctorSearch", doctorSearch);
        return "patient/search";
    }

    @GetMapping("/doctors/{id}")
    public String doctorDetail(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "patient/doctor-detail";
    }
}

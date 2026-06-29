package org.mabs.controller;

import org.mabs.entity.Appointment;
import org.mabs.entity.User;
import org.mabs.repository.AppointmentRepository;
import org.mabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentRepository appointmentRepository;

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

        List<Appointment> upcomingAppointments = appointmentRepository.findByPatientIdAndAppointmentTimeAfterAndStatusNotOrderByAppointmentTimeAsc(
                user.getId(),
                LocalDateTime.now(),
                "cancelled"
        );

        model.addAttribute("user", user);
        model.addAttribute("isProfileComplete", isProfileComplete);
        model.addAttribute("appointments", upcomingAppointments);

        // Patient-page
        return "patient-dashboard";
    }
}

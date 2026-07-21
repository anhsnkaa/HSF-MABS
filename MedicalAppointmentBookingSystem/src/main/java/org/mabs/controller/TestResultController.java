package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.entity.Appointment;
import org.mabs.entity.User;
import org.mabs.service.AppointmentService;
import org.mabs.service.TestResultService;
import org.mabs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;
    private final UserService userService;
    private final AppointmentService appointmentService;

    @GetMapping("/test-results")
    public String showTestResults(Model model, Principal principal) {
        User patient = userService.getUserByEmail(principal.getName());

        List<Appointment> appointments = appointmentService.getPatientAppointments(patient.getId());
        List<Appointment> validAppointments = appointments.stream()
                .filter(a -> List.of("confirmed", "completed").contains(a.getStatus()))
                .toList();

        model.addAttribute("appointments", validAppointments);
        model.addAttribute("testResults",
                testResultService.getTestResultsByPatient(patient.getId()));

        return "test-results";
    }

    @PostMapping("/test-results/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("appointmentId") Long appointmentId,
                             Principal principal,
                             RedirectAttributes ra) {
        User patient = userService.getUserByEmail(principal.getName());
        testResultService.uploadFile(file, appointmentId, patient.getId());
        ra.addFlashAttribute("message", "Tải file lên thành công");
        return "redirect:/test-results";
    }
}

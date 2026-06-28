package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.entity.User;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.UserRepository;
import org.mabs.service.TestResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @GetMapping("/test-results")
    public String showTestResults(Model model, Principal principal) {
        String email = principal.getName();
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("appointments",
                appointmentRepository.findByPatientId(patient.getId()));
        model.addAttribute("testResults",
                testResultService.getTestResultsByPatient(patient.getId()));

        return "test-results";
    }

    @PostMapping("/test-results/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("appointmentId") Long appointmentId,
                             Principal principal) {
        String email = principal.getName();
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!file.isEmpty()) {
            testResultService.uploadFile(file, appointmentId, patient.getId());
        }

        return "redirect:/test-results";
    }
}

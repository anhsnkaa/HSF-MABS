package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.entity.User;
import org.mabs.repository.UserRepository;
import org.mabs.service.MedicalRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final UserRepository userRepository;

    @GetMapping("/medical-records")
    public String viewMedicalRecords(Model model, Principal principal) {
        String email = principal.getName();
        User patient = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<MedicalRecordDto> records = medicalRecordService.getMedicalRecordsByPatient(patient.getId());
        model.addAttribute("records", records);
        return "medical-records";

    }
}

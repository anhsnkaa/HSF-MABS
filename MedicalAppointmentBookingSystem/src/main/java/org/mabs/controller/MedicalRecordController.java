package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.entity.User;
import org.mabs.service.MedicalRecordService;
import org.mabs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final UserService userService;

    @GetMapping("/medical-records")
    public String viewMedicalRecords(Model model, Principal principal) {
        User patient = userService.getUserByEmail(principal.getName());
        List<MedicalRecordDto> records = medicalRecordService.getMedicalRecordsByPatient(patient.getId());
        model.addAttribute("records", records);
        return "medical-records";

    }
}

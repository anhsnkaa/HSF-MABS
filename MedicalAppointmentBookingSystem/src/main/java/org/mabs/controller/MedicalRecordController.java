package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordResponse;
import org.mabs.service.MedicalRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping("/medical-records")
    public String viewMedicalRecords(Model model) {
        Long patientId = 4L;

        List<MedicalRecordResponse> records = medicalRecordService.getMedicalRecordsByPatient(patientId);
        model.addAttribute("records", records);
        return "medical-records";

    }
}

package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.entity.User;
import org.mabs.service.MedicalRecordService;
import org.mabs.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final UserService userService;

    @GetMapping("/medical-records")
    public String viewMedicalRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model,
            Principal principal) {

        User patient = userService.getUserByEmail(principal.getName());
        int pageSize = Math.min(Math.max(size, 3), 20);
        int pageIndex = Math.max(page, 0);

        Page<MedicalRecordDto> result = medicalRecordService.searchByPatient(
                patient.getId(), keyword, doctorId, fromDate, toDate,
                PageRequest.of(pageIndex, pageSize));

        boolean hasFilter = (keyword != null && !keyword.isBlank())
                || doctorId != null
                || fromDate != null
                || toDate != null;

        model.addAttribute("records", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("hasFilter", hasFilter);
        model.addAttribute("doctorList", medicalRecordService.getDoctorsByPatient(patient.getId()));
        return "medical-records";
    }
}

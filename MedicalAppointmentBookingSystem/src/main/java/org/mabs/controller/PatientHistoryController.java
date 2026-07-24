package org.mabs.controller;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.entity.Doctor;
import org.mabs.entity.User;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.UserRepository;
import org.mabs.service.MedicalRecordService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
@RequestMapping("/doctor/patient-history")
@RequiredArgsConstructor
public class PatientHistoryController {
    private final MedicalRecordService medicalRecordService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;  // (chưa dùng — để tương lai)
    private final DoctorRepository doctorRepository;

    @GetMapping
    public String view(@RequestParam("patientId") Long patientId,
                       Authentication auth,
                       Model model,
                       RedirectAttributes ra) {
        // 1. Verify doctor role
        try {
            resolveDoctorId(auth);
        } catch (IllegalStateException ex) {
            ra.addFlashAttribute("error", "Tài khoản của bạn chưa được thiết lập hồ sơ bác sĩ");
            return "redirect:/doctors/schedule";
        }

        // 2. Load patient
        User patient = userRepository.findById(patientId).orElse(null);
        if (patient == null || !"patient".equals(patient.getRole())) {
            ra.addFlashAttribute("error", "Không tìm thấy bệnh nhân");
            return "redirect:/doctors/schedule";
        }

        // 3. Load lịch sử
        List<MedicalRecordDto> records =
                medicalRecordService.getMedicalRecordsByPatient(patientId);

        model.addAttribute("patient", patient);
        model.addAttribute("records", records);
        return "doctor/patient-history";
    }

    private Long resolveDoctorId(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new IllegalStateException("Tài khoản không phải bác sĩ: " + email);
        }
        Doctor doctor = doctorRepository.findByUserId(user.getId()).orElse(null);
        if (doctor == null) {
            throw new IllegalStateException("Tài khoản không phải bác sĩ: " + email);
        }
        return doctor.getId();
    }
}

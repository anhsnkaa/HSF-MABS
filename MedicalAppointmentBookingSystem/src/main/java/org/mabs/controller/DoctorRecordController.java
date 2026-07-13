package org.mabs.controller;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorRecordFormDto;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.dto.AppointmentDTO;
import org.mabs.entity.Appointment;
import org.mabs.entity.Doctor;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.User;
import org.mabs.repository.*;
import org.mabs.service.DoctorScheduleService;
import org.mabs.service.MedicalRecordService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctor/record")
@RequiredArgsConstructor
public class DoctorRecordController {
    private final MedicalRecordService medicalRecordService;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorScheduleService doctorScheduleService;

    @GetMapping
    public String handle(@RequestParam(defaultValue = "create") String action,
                         @RequestParam(required = false) Long appointmentId,
                         @RequestParam(required = false) Long id,
                         @RequestParam(required = false) String error,
                         Authentication auth,
                         Model model,
                         RedirectAttributes ra) {
        switch (action) {
            case "create": return showCreateForm(appointmentId, auth, model, error);
            case "save":   // POST only → should not reach here in GET; falls through
            case "detail": return showDetail(id, model, auth);
            default:       return "redirect:/doctor/schedule";
        }
    }

    @PostMapping
    public String save(@ModelAttribute DoctorRecordFormDto form,
                       Authentication auth,
                       RedirectAttributes ra) {
        return handleSave(form, auth, ra);
    }

    // -------- private actions --------
    private String showCreateForm(Long appointmentId, Authentication auth,
                                  Model model, String error) {
        Long doctorId = resolveDoctorId(auth);
        AppointmentDTO apptDto;
        try {
            apptDto = doctorScheduleService.getAppointmentDetail(appointmentId);
        } catch (IllegalArgumentException ex) {
            return "redirect:/doctor/schedule?error=appointment_not_found";
        }
        if (apptDto.getDoctorId() == null || !apptDto.getDoctorId().equals(doctorId)) {
            return "redirect:/doctor/schedule?error=appointment_not_found";
        }
        // block nếu record đã tồn tại
        if (medicalRecordRepository.existsByAppointment_Id(appointmentId)) {
            return "redirect:/doctor/schedule?error=record_already_exists";
        }

        model.addAttribute("appointment", apptDto);
        model.addAttribute("form", new DoctorRecordFormDto());
        model.addAttribute("appointmentId", appointmentId);
        model.addAttribute("error", error);
        return "doctor/record-form";
    }

    private String handleSave(DoctorRecordFormDto form, Authentication auth,
                              RedirectAttributes ra) {
        if (form.getDiagnosis() == null || form.getDiagnosis().isBlank()) {
            ra.addAttribute("appointmentId", form.getAppointmentId());
            ra.addAttribute("error", "Vui lòng nhập chẩn đoán");
            return "redirect:/doctor/record?action=create";
        }
        try {
            Long doctorId = resolveDoctorId(auth);
            MedicalRecordDto saved = medicalRecordService.createRecord(
                    form.getAppointmentId(), doctorId,
                    form.getSymptoms(), form.getDiagnosis(), form.getNotes());
            ra.addFlashAttribute("success", "Đã lưu hồ sơ khám");
            return "redirect:/doctor/record?action=detail&id=" + saved.getId();
        } catch (IllegalStateException ex) {
            ra.addAttribute("appointmentId", form.getAppointmentId());
            ra.addAttribute("error", ex.getMessage());
            return "redirect:/doctor/record?action=create";
        } catch (IllegalArgumentException ex) {
            ra.addAttribute("appointmentId", form.getAppointmentId());
            ra.addAttribute("error", ex.getMessage());
            return "redirect:/doctor/record?action=create";
        }
    }

    private String showDetail(Long id, Model model, Authentication auth) {
        if (id == null) return "redirect:/doctor/schedule";
        Long doctorId = resolveDoctorId(auth);
        MedicalRecord mr = medicalRecordRepository.findById(id).orElse(null);
        if (mr == null || !mr.getDoctor().getId().equals(doctorId)) {
            return "redirect:/doctor/schedule?error=record_not_found";
        }
        // Tìm record trong danh sách medical_records của bệnh nhân
        List<MedicalRecordDto> allRecords = medicalRecordService.getMedicalRecordsByPatient(mr.getPatient().getId());
        MedicalRecordDto targetRecord = null;
        for (MedicalRecordDto r : allRecords) {
            if (r.getId().equals(id)) {
                targetRecord = r;
                break;
            }
        }
        model.addAttribute("record", targetRecord);
        return "doctor/record-detail";
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

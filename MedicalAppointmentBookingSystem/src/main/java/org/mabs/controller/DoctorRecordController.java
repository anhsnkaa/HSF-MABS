package org.mabs.controller;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.AppointmentDTO;
import org.mabs.dto.DoctorRecordFormDto;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.entity.Doctor;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.User;
import org.mabs.exception.AppointmentNotFoundException;
import org.mabs.exception.MedicalRecordAlreadyExistsException;
import org.mabs.exception.MedicalRecordNotFoundException;
import org.mabs.exception.ScheduleAccessDeniedException;
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
                         Authentication auth,
                         Model model) {
        switch (action) {
            case "create": return showCreateForm(appointmentId, auth, model);
            case "detail": return showDetail(id, model, auth);
            default:       return "redirect:/doctors/schedule";
        }
    }

    @PostMapping
    public String save(@ModelAttribute DoctorRecordFormDto form,
                       Authentication auth,
                       RedirectAttributes ra) {
        return handleSave(form, auth, ra);
    }

    private String showCreateForm(Long appointmentId, Authentication auth, Model model) {
        Long doctorId = resolveDoctorId(auth);

        AppointmentDTO apptDto = doctorScheduleService.getAppointmentDetail(appointmentId);

        if (apptDto.getDoctorId() == null || !apptDto.getDoctorId().equals(doctorId)) {
            throw new AppointmentNotFoundException("Không tìm thấy lịch hẹn");
        }
        if (medicalRecordRepository.existsByAppointment_Id(appointmentId)) {
            throw new MedicalRecordAlreadyExistsException("Hồ sơ khám cho lịch hẹn này đã được tạo");
        }

        model.addAttribute("appointment", apptDto);
        model.addAttribute("form", new DoctorRecordFormDto());
        model.addAttribute("appointmentId", appointmentId);
        return "doctor/record-form";
    }

    private String handleSave(DoctorRecordFormDto form, Authentication auth,
                              RedirectAttributes ra) {
        if (form.getDiagnosis() == null || form.getDiagnosis().isBlank()) {
            ra.addAttribute("appointmentId", form.getAppointmentId());
            ra.addFlashAttribute("error", "Vui lòng nhập chẩn đoán");
            return "redirect:/doctor/record?action=create";
        }

        Long doctorId = resolveDoctorId(auth);
        MedicalRecordDto saved = medicalRecordService.createRecord(
                form.getAppointmentId(), doctorId,
                form.getSymptoms(), form.getDiagnosis(), form.getNotes());
        ra.addFlashAttribute("success", "Đã lưu hồ sơ khám");
        return "redirect:/doctor/record?action=detail&id=" + saved.getId();
    }

    private String showDetail(Long id, Model model, Authentication auth) {
        if (id == null) return "redirect:/doctors/schedule";
        Long doctorId = resolveDoctorId(auth);
        MedicalRecord mr = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Không tìm thấy hồ sơ khám"));
        if (!mr.getDoctor().getId().equals(doctorId)) {
            throw new MedicalRecordNotFoundException("Không tìm thấy hồ sơ khám");
        }

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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ScheduleAccessDeniedException("Tài khoản của bạn chưa được thiết lập hồ sơ bác sĩ"));
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ScheduleAccessDeniedException("Tài khoản của bạn chưa được thiết lập hồ sơ bác sĩ"));
        return doctor.getId();
    }
}

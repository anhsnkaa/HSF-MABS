package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.PrescriptionDto;
import org.mabs.dto.PrescriptionRequestDto;
import org.mabs.entity.Doctor;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.Medicine;
import org.mabs.entity.User;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.MedicalRecordRepository;
import org.mabs.repository.UserRepository;
import org.mabs.service.MedicineService;
import org.mabs.service.PrescriptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctor/prescription")
@RequiredArgsConstructor
public class DoctorPrescriptionController {

    private final PrescriptionService prescriptionService;
    private final MedicineService medicineService;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public String form(@RequestParam("recordId") Long recordId,
                       Authentication auth,
                       Model model,
                       RedirectAttributes ra) {
        Long doctorId = resolveDoctorId(auth);

        MedicalRecord record = medicalRecordRepository.findByIdWithDetails(recordId).orElse(null);
        if (record == null) {
            ra.addFlashAttribute("error", "Không tìm thấy hồ sơ");
            return "redirect:/doctor/schedule";
        }
        if (record.getDoctor() == null || !record.getDoctor().getId().equals(doctorId)) {
            ra.addFlashAttribute("error", "Bạn không có quyền truy cập hồ sơ này");
            return "redirect:/doctor/schedule";
        }

        List<PrescriptionDto> currentPrescriptions =
                prescriptionService.getPrescriptionsByRecordId(recordId);
        List<Medicine> activeMedicines = medicineService.getActiveMedicines();

        model.addAttribute("record", record);
        model.addAttribute("prescriptions", currentPrescriptions);
        model.addAttribute("medicines", activeMedicines);
        model.addAttribute("form", new PrescriptionRequestDto());
        model.addAttribute("recordId", recordId);
        return "doctor/prescription-form";
    }

    @PostMapping(params = "action=add")
    public String add(@ModelAttribute PrescriptionRequestDto form,
                      Authentication auth,
                      RedirectAttributes ra) {
        Long doctorId = resolveDoctorId(auth);
        try {
            prescriptionService.addPrescription(form, doctorId);
            ra.addFlashAttribute("success", "Đã thêm thuốc vào đơn");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/doctor/prescription?action=form&recordId=" + form.getMedicalRecordId();
    }

    @PostMapping(params = "action=remove")
    public String remove(@RequestParam("prescriptionId") Long prescriptionId,
                         @RequestParam("recordId") Long recordId,
                         Authentication auth,
                         RedirectAttributes ra) {
        Long doctorId = resolveDoctorId(auth);
        try {
            prescriptionService.removePrescription(prescriptionId, doctorId);
            ra.addFlashAttribute("success", "Đã xoá thuốc khỏi đơn");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/doctor/prescription?action=form&recordId=" + recordId;
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

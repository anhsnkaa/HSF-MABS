package org.mabs.controller;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.entity.Doctor;
import org.mabs.entity.User;
import org.mabs.exception.PatientNotFoundException;
import org.mabs.exception.ScheduleAccessDeniedException;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.UserRepository;
import org.mabs.service.MedicalRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/doctor/patient-history")
@RequiredArgsConstructor
public class PatientHistoryController {
    private final MedicalRecordService medicalRecordService;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @GetMapping
    public String view(@RequestParam("patientId") Long patientId,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       Authentication auth,
                       Model model) {
        resolveDoctorId(auth);

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Không tìm thấy bệnh nhân"));

        if (!"patient".equals(patient.getRole())) {
            throw new PatientNotFoundException("Không tìm thấy bệnh nhân");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<MedicalRecordDto> recordsPage =
                medicalRecordService.getMedicalRecordsByPatientPage(patientId, pageable);

        model.addAttribute("patient", patient);
        model.addAttribute("records", recordsPage.getContent());
        model.addAttribute("currentPage", recordsPage.getNumber());
        model.addAttribute("totalPages", recordsPage.getTotalPages());
        model.addAttribute("totalItems", recordsPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("patientId", patientId);
        return "doctor/patient-history";
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

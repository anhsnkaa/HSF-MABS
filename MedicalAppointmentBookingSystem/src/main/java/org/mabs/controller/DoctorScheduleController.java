package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.AppointmentDTO;
import org.mabs.entity.Doctor;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.UserRepository;
import org.mabs.service.DoctorScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorScheduleController {
    private final DoctorScheduleService scheduleService;
    private final DoctorRepository doctorRepo;
    private final UserRepository userRepo;
    private final AppointmentRepository appointmentRepository;

    @GetMapping("/schedule")
    public String handle(
            @RequestParam(defaultValue = "list") String action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long id,
            Authentication auth,
            Model model
            ){
        if("detail".equals(action)){
            return showDetail(id,model);
        }
    return showList(date,status,auth,model);
    }

    private String showDetail(Long id, Model model) {
        AppointmentDTO dto = scheduleService.getAppointmentDetail(id);
        model.addAttribute("appointment",dto);
        return "doctor/appointment-detail";
    }

    private String showList(LocalDate date, String statusFilter, Authentication auth, Model model) {
        Long doctorId = resolveDoctorId(auth);
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        List<AppointmentDTO> appointments = scheduleService.getAppointmentsByDate(doctorId,targetDate,statusFilter);
        model.addAttribute("appointments", appointments);
        model.addAttribute("date", targetDate);
        model.addAttribute("status", statusFilter);

        return "doctor/schedule";
    }

    private Long resolveDoctorId(Authentication auth) {
        String email = auth.getName();
        return userRepo.findByEmail(email).flatMap(u -> doctorRepo.findByUserId(u.getId())).map(Doctor::getId).orElseThrow(() -> new IllegalStateException("Tài khoản không phải bác sĩ: " + email));
    }

}

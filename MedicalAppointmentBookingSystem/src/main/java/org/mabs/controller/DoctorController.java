package org.mabs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.AppointmentDTO;
import org.mabs.dto.DoctorCreationDto;
import org.mabs.dto.DoctorUpdateDto;
import org.mabs.entity.Doctor;
import org.mabs.entity.User;
import org.mabs.exception.DoctorNotFoundException;
import org.mabs.exception.ScheduleAccessDeniedException;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.UserRepository;

import org.mabs.service.DoctorScheduleService;
import org.mabs.service.DoctorService;
import org.mabs.service.SpecialtyService;
import org.mabs.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final UserService userService;
    private final SpecialtyService specialtyService;
    private final DoctorScheduleService scheduleService;
    private final DoctorRepository doctorRepo;
    private final UserRepository userRepo;


    @GetMapping("/admin/doctors")
    public String getAllDoctors(Model model) {
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        return "/admin/doctor/doctor-list";
    }

    @GetMapping("/admin/doctors/add")
    public String addDoctor(Model model) {
        model.addAttribute("dto", new DoctorCreationDto());
        model.addAttribute("doctorRoleList", userService.getRoleDoctorWithNoProfile());
        model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
        return "/admin/doctor/doctor-add";
    }

    @GetMapping("/admin/doctors/update/{id}")
    public String updateDoctor(@PathVariable(name = "id") Long id,
                               Model model) {
        Doctor doctor = doctorService.findById(id);
        DoctorUpdateDto dto = new DoctorUpdateDto();
        dto.setId(doctor.getId());
        dto.setUserId(doctor.getUser().getId());
        dto.setSpecialtyId(doctor.getSpecialty().getId());
        dto.setTitle(doctor.getTitle());
        dto.setBio(doctor.getBio());
        dto.setConsultationFee(doctor.getConsultationFee());
        dto.setExperienceYears(doctor.getExperienceYears());

        model.addAttribute("dto", dto);
        model.addAttribute("doctorRoleList", userService.getRoleDoctor());
        model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
        return "/admin/doctor/doctor-update";
    }

    @PostMapping("/admin/doctors/add")
    public String addDoctor(@Valid @ModelAttribute("dto") DoctorCreationDto dto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctorRoleList", userService.getRoleDoctorWithNoProfile());
            model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
            return "/admin/doctor/doctor-add";
        }

        Doctor doctor = new Doctor();
        doctor.setUser(userService.findById(dto.getUserId()));
        doctor.setSpecialty(specialtyService.findById(dto.getSpecialtyId()));
        doctor.setTitle(dto.getTitle());
        doctor.setBio(dto.getBio());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setExperienceYears(dto.getExperienceYears());

        doctorService.createDoctor(doctor);
        redirectAttributes.addFlashAttribute("message", "Added successfully");
        return "redirect:/admin/doctors";
    }

    @PostMapping("/admin/doctors/update/{id}")
    public String updateDoctor(@PathVariable(name = "id") Long id,
                               @Valid @ModelAttribute(name = "dto") DoctorUpdateDto dto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctorRoleList", userService.getRoleDoctor());
            model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
            return "/admin/doctor/doctor-update";
        }

        Doctor doctor = doctorService.findById(dto.getId());
        doctor.setUser(userService.findById(dto.getUserId()));
        doctor.setSpecialty(specialtyService.findById(dto.getSpecialtyId()));
        doctor.setTitle(dto.getTitle());
        doctor.setBio(dto.getBio());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setExperienceYears(dto.getExperienceYears());

        doctorService.updateDoctor(doctor);
        redirectAttributes.addFlashAttribute("message", "Updated successfully");
        return "redirect:/admin/doctors";
    }

    @GetMapping("/doctors/schedule")
    public String handleSchedule(
            @RequestParam(defaultValue = "list") String action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long id,
            Authentication auth,
            Model model
    ) {
        if ("detail".equals(action)) {
            AppointmentDTO dto = scheduleService.getAppointmentDetail(id);
            model.addAttribute("appointment", dto);
            return "doctor/appointment-detail";
        }
        return showScheduleList(date, status, auth, model);
    }

    private String showScheduleList(LocalDate date, String statusFilter, Authentication auth, Model model) {
        Long doctorId = resolveDoctorId(auth);
        List<AppointmentDTO> appointments;
        if (date != null) {
            appointments = scheduleService.getAppointmentsByDate(doctorId, date, statusFilter);
        } else {
            appointments = scheduleService.getAppointments(doctorId, statusFilter);
        }
        model.addAttribute("appointments", appointments);
        model.addAttribute("date", date);
        model.addAttribute("status", statusFilter);
        model.addAttribute("today", LocalDate.now());

        return "doctor/schedule";
    }

    private Long resolveDoctorId(Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Không tìm thấy user với email: " + email));

        Doctor doctor = doctorRepo.findByUserId(user.getId())
                .orElseThrow(() -> new ScheduleAccessDeniedException("Tài khoản không phải bác sĩ"));

        return doctor.getId();
    }

    @PostMapping("/delete")
    public String deleteDoctor(@RequestParam(name = "id") Long id,
                               RedirectAttributes redirectAttributes) {
        doctorService.deleteDoctor(id);
        redirectAttributes.addFlashAttribute("message", "Deleted successfully");
        return "redirect:/doctors";
    }

}

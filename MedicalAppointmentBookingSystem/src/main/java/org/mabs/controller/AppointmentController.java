package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.BookingRequest;
import org.mabs.dto.CancelRequest;
import org.mabs.dto.RescheduleRequest;
import org.mabs.entity.Appointment;
import org.mabs.entity.User;
import org.mabs.entity.WorkingSchedule;
import org.mabs.entity.Doctor;
import org.mabs.service.AppointmentService;
import org.mabs.service.DoctorService;
import org.mabs.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final DoctorService doctorService;

    @GetMapping("/book-appointment")
    public String showBookForm(@RequestParam(required = false) Long doctorId, Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);

        BookingRequest bookingRequest = new BookingRequest();
        if (doctorId != null) {
            bookingRequest.setDoctorId(doctorId);
            List<WorkingSchedule> schedules = appointmentService.getWorkingSchedules(doctorId);
            model.addAttribute("schedules", schedules);
            Doctor selectedDoctor = doctorService.getDoctorById(doctorId);
            model.addAttribute("selectedDoctor", selectedDoctor);
        }
        model.addAttribute("bookingRequest", bookingRequest);
        return "patient/book-appointment";
    }

    @PostMapping("/book-appointment")
    public String bookAppointment(@ModelAttribute BookingRequest bookingRequest,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";
        try {
            User user = userService.getUserByEmail(principal.getName());
            appointmentService.bookAppointment(bookingRequest, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Đặt lịch thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đặt lịch thất bại: " + e.getMessage());
        }
        return "redirect:/appointments";
    }

    @GetMapping("/appointments")
    public String listAppointments(Principal principal,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model) {
        if (principal == null) return "redirect:/login";
        int pageSize = Math.min(Math.max(size, 5), 20);
        int pageIndex = Math.max(page, 0);

        User user = userService.getUserByEmail(principal.getName());
        Page<Appointment> appointmentPage = appointmentService.getPatientAppointments(user.getId(), PageRequest.of(pageIndex, pageSize));
        model.addAttribute("appointments", appointmentPage.getContent());
        model.addAttribute("page", appointmentPage);
        return "patient/appointments";
    }

    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id,
                                    @RequestParam String cancelReason,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";
        try {
            User user = userService.getUserByEmail(principal.getName());
            CancelRequest request = new CancelRequest();
            request.setAppointmentId(id);
            request.setCancelReason(cancelReason);
            appointmentService.cancelAppointment(request, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Hủy lịch thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hủy lịch thất bại: " + e.getMessage());
        }
        return "redirect:/appointments";
    }

    @GetMapping("/appointments/{id}/reschedule")
    public String showRescheduleForm(@PathVariable Long id, Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        User user = userService.getUserByEmail(principal.getName());
        Appointment appointment = appointmentService.findByIdAndPatientId(id, user.getId());

        List<Doctor> doctors = doctorService.getAllDoctors();

        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctors);
        model.addAttribute("rescheduleRequest", new RescheduleRequest());

        List<WorkingSchedule> schedules = appointmentService.getWorkingSchedules(appointment.getDoctor().getId());
        model.addAttribute("schedules", schedules);

        return "patient/reschedule-appointment";
    }

    @PostMapping("/appointments/{id}/reschedule")
    public String rescheduleAppointment(@PathVariable Long id,
                                        @ModelAttribute RescheduleRequest rescheduleRequest,
                                        Principal principal,
                                        RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";
        try {
            User user = userService.getUserByEmail(principal.getName());
            rescheduleRequest.setAppointmentId(id);
            appointmentService.rescheduleAppointment(rescheduleRequest, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Dời lịch thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dời lịch thất bại: " + e.getMessage());
        }
        return "redirect:/appointments";
    }
}
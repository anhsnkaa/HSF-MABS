package org.mabs.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentNotFoundException.class)
    public String handleAppointmentNotFound(AppointmentNotFoundException ex, RedirectAttributes redirectAttributes) {
        log.warn("AppointmentNotFoundException: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(ScheduleAccessDeniedException.class)
    public String handleScheduleAccessDenied(ScheduleAccessDeniedException ex, RedirectAttributes redirectAttributes) {
        log.warn("ScheduleAccessDeniedException: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public String handleDoctorNotFound(DoctorNotFoundException ex, RedirectAttributes redirectAttributes) {
        log.warn("DoctorNotFoundException: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/admin/doctors";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, RedirectAttributes redirectAttributes) {
        log.warn("IllegalStateException: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        log.warn("IllegalArgumentException: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmail(DuplicateEmailException ex, RedirectAttributes redirectAttributes) {
        log.warn("DuplicateEmailException: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/accounts";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, RedirectAttributes redirectAttributes) {
        log.error("Unhandled exception", ex);
        redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi hệ thống: " + ex.getMessage());
        return "redirect:/home";
    }
}

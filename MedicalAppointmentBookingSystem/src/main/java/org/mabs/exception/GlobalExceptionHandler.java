package org.mabs.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentNotFoundException.class)
    public String handleAppointmentNotFound(AppointmentNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(MedicalRecordNotFoundException.class)
    public String handleMedicalRecordNotFound(MedicalRecordNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(MedicalRecordAlreadyExistsException.class)
    public String handleMedicalRecordAlreadyExists(MedicalRecordAlreadyExistsException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public String handlePatientNotFound(PatientNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(ScheduleAccessDeniedException.class)
    public String handleScheduleAccessDenied(ScheduleAccessDeniedException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public String handleDoctorNotFound(DoctorNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/admin/doctors";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmail(DuplicateEmailException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/accounts";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/doctors/schedule";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi hệ thống: " + ex.getMessage());
        return "redirect:/home";
    }
}

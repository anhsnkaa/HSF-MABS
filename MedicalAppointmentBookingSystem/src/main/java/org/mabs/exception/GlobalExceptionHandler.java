package org.mabs.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ResourceNotFoundException.class,
            ConflictException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            AppointmentNotFoundException.class,
            DoctorNotFoundException.class
    })
    public String handleBusiness(RuntimeException ex,
                                 RedirectAttributes ra,
                                 HttpServletRequest request) {
        log.warn("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        ra.addFlashAttribute("error", ex.getMessage());
        return "redirect:" + resolveRedirect(request.getRequestURI());
    }

    @ExceptionHandler(ScheduleAccessDeniedException.class)
    public String handleScheduleAccessDenied(ScheduleAccessDeniedException ex, RedirectAttributes ra) {
        log.warn("ScheduleAccessDeniedException: {}", ex.getMessage());
        ra.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmail(DuplicateEmailException ex, RedirectAttributes ra) {
        log.warn("DuplicateEmailException: {}", ex.getMessage());
        ra.addFlashAttribute("error", ex.getMessage());
        return "redirect:/accounts";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex,
                                RedirectAttributes ra,
                                HttpServletRequest request) {
        log.error("Unhandled exception", ex);
        ra.addFlashAttribute("error", "Đã xảy ra lỗi hệ thống.");
        return "redirect:" + resolveRedirect(request.getRequestURI());
    }

    private String resolveRedirect(String uri) {
        if (uri == null || uri.isBlank()) {
            return "/home";
        }
        if (uri.startsWith("/schedules")) {
            return "/schedules";
        }
        if (uri.startsWith("/test-results")) {
            return "/test-results";
        }
        if (uri.startsWith("/medical-records")) {
            return "/medical-records";
        }
        if (uri.startsWith("/doctor/record")
                || uri.startsWith("/doctor/prescription")
                || uri.startsWith("/doctor/patient-history")
                || uri.startsWith("/doctors/schedule")) {
            return "/doctors/schedule";
        }
        if (uri.startsWith("/admin/doctors")) {
            return "/admin/doctors";
        }
        if (uri.startsWith("/accounts")) {
            return "/accounts";
        }
        if (uri.startsWith("/specialties")) {
            return "/specialties";
        }
        if (uri.startsWith("/book-appointment") || uri.startsWith("/appointments")) {
            return "/appointments";
        }
        return "/home";
    }
}

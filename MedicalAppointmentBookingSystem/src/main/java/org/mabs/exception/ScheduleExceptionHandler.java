package org.mabs.exception;

import org.mabs.controller.WorkingScheduleController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(assignableTypes = WorkingScheduleController.class)
public class ScheduleExceptionHandler {

    @ExceptionHandler({
            ResourceNotFoundException.class,
            ConflictException.class,
            IllegalArgumentException.class
    })
    public String handleScheduleException(RuntimeException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return "redirect:/schedules";
    }
}

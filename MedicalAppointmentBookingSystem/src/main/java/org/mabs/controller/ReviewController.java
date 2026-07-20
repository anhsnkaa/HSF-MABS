package org.mabs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.ReviewDTO;
import org.mabs.entity.User;
import org.mabs.service.ReviewService;
import org.mabs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping("/patient/reviews")
    public String submitReview(@Valid @ModelAttribute ReviewDTO reviewDTO,
                               BindingResult bindingResult,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", "Đánh giá thất bại: " + errorMsg);
            return "redirect:/appointments";
        }

        try {
            User user = userService.getUserByEmail(principal.getName());
            reviewService.submitReview(reviewDTO, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Đánh giá bác sĩ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đánh giá thất bại: " + e.getMessage());
        }

        return "redirect:/appointments";
    }
}


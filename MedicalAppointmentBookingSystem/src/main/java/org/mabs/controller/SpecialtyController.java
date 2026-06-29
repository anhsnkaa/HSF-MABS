package org.mabs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.AccountCreationDto;
import org.mabs.dto.SpecialtyCreationDto;
import org.mabs.entity.Specialty;
import org.mabs.service.SpecialtyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/specialties")
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    @GetMapping
    public String getAllSpecialties(Model model) {
        model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
        return "/admin/specialty/specialty-list";
    }

    @GetMapping("/add")
    public String createSpecialty(Model model) {
        model.addAttribute("dto", new SpecialtyCreationDto());
        return "/admin/specialty/specialty-add";
    }

    @PostMapping("/add")
    public String createSpecialty(@Valid @ModelAttribute("dto") SpecialtyCreationDto dto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/admin/specialty/specialty-add";
        }

        Specialty specialty = new Specialty();
        specialty.setName(dto.getName());
        specialty.setDescription(dto.getDescription());
        specialtyService.createSpecialty(specialty);

        redirectAttributes.addFlashAttribute("message", "Added successfully!");
        return "redirect:/specialties";
    }

}

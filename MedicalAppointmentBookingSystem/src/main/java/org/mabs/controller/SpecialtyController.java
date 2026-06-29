package org.mabs.controller;

import lombok.RequiredArgsConstructor;
import org.mabs.service.SpecialtyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/specialties")
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    @GetMapping
    public String getAllSpecialties(Model model) {
        model.addAttribute("specialtyList", specialtyService.getALlSpecialties());
        return "/admin/admin-specialty";
    }

}

package org.mabs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mabs.dto.ScheduleBulkResult;
import org.mabs.dto.WorkingScheduleCreationDto;
import org.mabs.dto.WorkingScheduleMonthDto;
import org.mabs.dto.WorkingScheduleUpdateDto;
import org.mabs.entity.WorkingSchedule;
import org.mabs.service.DoctorService;
import org.mabs.service.WorkingScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class WorkingScheduleController {

    private final WorkingScheduleService workingScheduleService;
    private final DoctorService doctorService;

    @GetMapping
    public String getWorkingSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {

        int pageSize = Math.min(Math.max(size, 5), 50);
        int pageIndex = Math.max(page, 0);

        Page<WorkingSchedule> result = workingScheduleService.searchSchedules(
                keyword, doctorId, status, year, month, PageRequest.of(pageIndex, pageSize));

        model.addAttribute("workingSchedules", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("status", status);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        return "/admin/schedule/schedule-list";
    }

    @GetMapping("/add")
    public String addWorkingScheduleForm(Model model) {
        model.addAttribute("dto", new WorkingScheduleCreationDto());
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        return "/admin/schedule/schedule-add";
    }

    @PostMapping("/add")
    public String addWorkingSchedule(@Valid @ModelAttribute("dto") WorkingScheduleCreationDto dto,
                                     BindingResult bindingResult,
                                     Model model,
                                     RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctorList", doctorService.getAllDoctors());
            return "/admin/schedule/schedule-add";
        }
        workingScheduleService.createWorkingSchedule(dto);
        ra.addFlashAttribute("message", "Thêm lịch làm việc thành công!");
        return "redirect:/schedules";
    }

    @GetMapping("/add-month")
    public String addMonthlyScheduleForm(Model model) {
        WorkingScheduleMonthDto dto = new WorkingScheduleMonthDto();
        dto.setYear(java.time.LocalDate.now().getYear());
        dto.setMonth(java.time.LocalDate.now().getMonthValue());
        model.addAttribute("dto", dto);
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        return "/admin/schedule/schedule-add-month";
    }

    @PostMapping("/add-month")
    public String addMonthlySchedule(@Valid @ModelAttribute("dto") WorkingScheduleMonthDto dto,
                                     BindingResult bindingResult,
                                     Model model,
                                     RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctorList", doctorService.getAllDoctors());
            return "/admin/schedule/schedule-add-month";
        }
        ScheduleBulkResult result = workingScheduleService.createMonthlySchedule(dto);
        ra.addFlashAttribute("message",
                "Tạo lịch tháng " + dto.getMonth() + "/" + dto.getYear()
                        + ": thành công " + result.getCreated() + " ngày, bỏ qua "
                        + result.getSkipped() + " ngày (đã có slot).");
        return "redirect:/schedules";
    }

    @PostMapping("/update-form")
    public String updateWorkingScheduleForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("dto", workingScheduleService.findWorkingScheduleById(id));
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        return "/admin/schedule/schedule-update";
    }

    @PostMapping("/update")
    public String updateWorkingSchedule(@Valid @ModelAttribute("dto") WorkingScheduleUpdateDto dto,
                                        BindingResult bindingResult,
                                        Model model,
                                        RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctorList", doctorService.getAllDoctors());
            return "/admin/schedule/schedule-update";
        }
        workingScheduleService.updateWorkingSchedule(dto);
        ra.addFlashAttribute("message", "Cập nhật thành công!");
        return "redirect:/schedules";
    }

    @PostMapping("/delete")
    public String deleteWorkingSchedule(@RequestParam("id") Long id, RedirectAttributes ra) {
        workingScheduleService.deleteWorkingSchedule(id);
        ra.addFlashAttribute("message", "Xóa thành công!");
        return "redirect:/schedules";
    }
}

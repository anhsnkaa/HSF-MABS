package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.ScheduleBulkResult;
import org.mabs.dto.WorkingScheduleCreationDto;
import org.mabs.dto.WorkingScheduleMonthDto;
import org.mabs.dto.WorkingScheduleUpdateDto;
import org.mabs.entity.Doctor;
import org.mabs.entity.WorkingSchedule;
import org.mabs.exception.ConflictException;
import org.mabs.exception.ResourceNotFoundException;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.WorkingScheduleRepository;
import org.mabs.service.WorkingScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingScheduleServiceImpl implements WorkingScheduleService {

    private final WorkingScheduleRepository workingScheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<WorkingSchedule> getWorkingSchedules() {
        return workingScheduleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkingSchedule> searchSchedules(String keyword, Long doctorId, String status,
                                                 Integer year, Integer month, Pageable pageable) {
        String normalizedKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        String normalizedStatus = (status != null && !status.isBlank()) ? status.trim() : null;

        LocalDate fromDate = null;
        LocalDate toDate = null;
        if (year != null) {
            if (month != null && month >= 1 && month <= 12) {
                fromDate = LocalDate.of(year, month, 1);
                toDate = fromDate.withDayOfMonth(fromDate.lengthOfMonth());
            } else {
                fromDate = LocalDate.of(year, 1, 1);
                toDate = LocalDate.of(year, 12, 31);
            }
        }

        return workingScheduleRepository.search(
                doctorId, normalizedStatus, normalizedKeyword, fromDate, toDate, pageable);
    }

    @Override
    @Transactional
    public WorkingSchedule createWorkingSchedule(WorkingScheduleCreationDto dto) {
        validateTime(dto.getStartTime(), dto.getEndTime(), dto.getSlotMinutes());

        if (workingScheduleRepository.existsByDoctorIdAndWorkDateAndStartTime(
                dto.getDoctorId(), dto.getWorkDate(), dto.getStartTime())) {
            throw new ConflictException("Slot này đã tồn tại cho bác sĩ");
        }

        return workingScheduleRepository.save(toEntity(dto));
    }

    @Override
    @Transactional
    public ScheduleBulkResult createMonthlySchedule(WorkingScheduleMonthDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ"));

        validateTime(dto.getStartTime(), dto.getEndTime(), dto.getSlotMinutes());

        LocalDate start = LocalDate.of(dto.getYear(), dto.getMonth(), 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        int slotMinutes = dto.getSlotMinutes() != null ? dto.getSlotMinutes() : 30;
        String status = normalizeStatus(dto.getStatus());

        int created = 0;
        int skipped = 0;

        for (LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)) {
            if (workingScheduleRepository.existsByDoctorIdAndWorkDateAndStartTime(
                    dto.getDoctorId(), day, dto.getStartTime())) {
                skipped++;
                continue;
            }

            WorkingSchedule schedule = new WorkingSchedule();
            schedule.setDoctor(doctor);
            schedule.setWorkDate(day);
            schedule.setStartTime(dto.getStartTime());
            schedule.setEndTime(dto.getEndTime());
            schedule.setSlotMinutes(slotMinutes);
            schedule.setStatus(status);
            workingScheduleRepository.save(schedule);
            created++;
        }

        return new ScheduleBulkResult(created, skipped);
    }

    @Override
    @Transactional
    public WorkingSchedule updateWorkingSchedule(WorkingScheduleUpdateDto dto) {
        WorkingSchedule existing = workingScheduleRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch làm việc"));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ"));

        validateTime(dto.getStartTime(), dto.getEndTime(), dto.getSlotMinutes());

        if (workingScheduleRepository.existsByDoctorIdAndWorkDateAndStartTimeAndIdNot(
                dto.getDoctorId(), dto.getWorkDate(), dto.getStartTime(), dto.getId())) {
            throw new ConflictException("Slot này đã tồn tại cho bác sĩ");
        }

        existing.setDoctor(doctor);
        existing.setWorkDate(dto.getWorkDate());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setSlotMinutes(dto.getSlotMinutes() != null ? dto.getSlotMinutes() : 30);
        existing.setStatus(normalizeStatus(dto.getStatus()));
        return workingScheduleRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteWorkingSchedule(Long id) {
        WorkingSchedule schedule = workingScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch làm việc"));

        if (appointmentRepository.existsByWorkingScheduleId(id)) {
            throw new ConflictException("Không thể xóa lịch đang có lịch hẹn");
        }

        workingScheduleRepository.delete(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkingScheduleUpdateDto findWorkingScheduleById(Long id) {
        WorkingSchedule workingSchedule = workingScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch làm việc"));
        return toUpdateDto(workingSchedule);
    }

    private void validateTime(LocalTime startTime, LocalTime endTime, Integer slotMinutes) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Giờ bắt đầu và giờ kết thúc không được để trống");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu");
        }
        if (slotMinutes != null && slotMinutes <= 0) {
            throw new IllegalArgumentException("Slot minutes phải > 0");
        }
    }

    private WorkingScheduleUpdateDto toUpdateDto(WorkingSchedule workingSchedule) {
        WorkingScheduleUpdateDto dto = new WorkingScheduleUpdateDto();
        dto.setId(workingSchedule.getId());
        dto.setDoctorId(workingSchedule.getDoctor().getId());
        dto.setWorkDate(workingSchedule.getWorkDate());
        dto.setStartTime(workingSchedule.getStartTime());
        dto.setEndTime(workingSchedule.getEndTime());
        dto.setSlotMinutes(workingSchedule.getSlotMinutes());
        dto.setStatus(workingSchedule.getStatus());
        return dto;
    }

    private WorkingSchedule toEntity(WorkingScheduleCreationDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ"));

        WorkingSchedule workingSchedule = new WorkingSchedule();
        workingSchedule.setDoctor(doctor);
        workingSchedule.setWorkDate(dto.getWorkDate());
        workingSchedule.setStartTime(dto.getStartTime());
        workingSchedule.setEndTime(dto.getEndTime());
        workingSchedule.setSlotMinutes(dto.getSlotMinutes() != null ? dto.getSlotMinutes() : 30);
        workingSchedule.setStatus(normalizeStatus(dto.getStatus()));
        return workingSchedule;
    }

    private String normalizeStatus(String status) {
        return (status != null && !status.isBlank()) ? status : "open";
    }
}

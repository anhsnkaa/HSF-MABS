package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.AppointmentDTO;
import org.mabs.entity.Appointment;
import org.mabs.entity.WorkingSchedule;
import org.mabs.exception.AppointmentNotFoundException;
import org.mabs.repository.AppointmentRepository;
import org.mabs.service.DoctorScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDate(Long doctorId, LocalDate date, String statusFilter) {
        if (doctorId == null || date == null) {
            return Collections.emptyList();
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<Appointment> all = appointmentRepository.findByDoctorAndDateRange(doctorId, startOfDay, endOfDay);

        return filterAndConvert(all, statusFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointments(Long doctorId, String statusFilter) {
        if (doctorId == null) {
            return Collections.emptyList();
        }

        List<Appointment> all = appointmentRepository.findByDoctorIdOrderByAppointmentTimeAsc(doctorId);
        return filterAndConvert(all, statusFilter);
    }

    private List<AppointmentDTO> filterAndConvert(List<Appointment> all, String statusFilter) {
        if (all == null || all.isEmpty()) {
            return Collections.emptyList();
        }

        List<Appointment> filtered = new ArrayList<>();
        if (statusFilter != null && !statusFilter.isBlank()) {
            for (Appointment a : all) {
                if (statusFilter.equals(a.getStatus())) {
                    filtered.add(a);
                }
            }
        } else {
            filtered = all;
        }

        List<AppointmentDTO> dtos = new ArrayList<>();
        for (Appointment a : filtered) {
            String slotTimeRange = formatSchedule(a.getWorkingSchedule());
            dtos.add(AppointmentDTO.fromEntity(a, slotTimeRange));
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentDetail(Long appointmentId) {
        Appointment a = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Không tìm thấy lịch hẹn với ID: " + appointmentId));

        return AppointmentDTO.fromEntity(a, formatSchedule(a.getWorkingSchedule()));
    }

    private String formatSchedule(WorkingSchedule schedule) {
        if (schedule == null) return "";
        return schedule.getStartTime() + " - " + schedule.getEndTime();
    }

}

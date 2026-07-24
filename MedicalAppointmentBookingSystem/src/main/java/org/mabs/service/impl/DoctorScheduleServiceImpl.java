package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.AppointmentDTO;
import org.mabs.entity.Appointment;
import org.mabs.entity.WorkingSchedule;
import org.mabs.exception.AppointmentNotFoundException;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.MedicalRecordRepository;
import org.mabs.service.DoctorScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;

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

        List<Appointment> all = appointmentRepository.findByDoctorIdOrderByAppointmentTimeDesc(doctorId);
        return filterAndConvert(all, statusFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsPage(Long doctorId, String statusFilter, Pageable pageable) {
        if (doctorId == null) {
            return Page.empty(pageable);
        }

        Page<Appointment> page = (statusFilter == null || statusFilter.isBlank())
                ? appointmentRepository.findPageByDoctorId(doctorId, pageable)
                : appointmentRepository.findPageByDoctorIdAndStatus(doctorId, statusFilter, pageable);

        if (page.isEmpty()) {
            return page.map(a -> AppointmentDTO.fromEntity(a, formatSchedule(a.getWorkingSchedule()), false));
        }

        Set<Long> withRecord = new HashSet<>(
                medicalRecordRepository.findAppointmentIdsHavingRecord(
                        page.getContent().stream()
                                .map(Appointment::getId)
                                .collect(Collectors.toList())));

        return page.map(a -> AppointmentDTO.fromEntity(
                a,
                formatSchedule(a.getWorkingSchedule()),
                withRecord.contains(a.getId())));
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

        Set<Long> withRecord = new HashSet<>();
        List<Long> ids = filtered.stream().map(Appointment::getId).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            withRecord = new HashSet<>(medicalRecordRepository.findAppointmentIdsHavingRecord(ids));
        }

        List<AppointmentDTO> dtos = new ArrayList<>();
        for (Appointment a : filtered) {
            String slotTimeRange = formatSchedule(a.getWorkingSchedule());
            boolean hasRecord = withRecord.contains(a.getId());
            dtos.add(AppointmentDTO.fromEntity(a, slotTimeRange, hasRecord));
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentDetail(Long appointmentId) {
        Appointment a = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Không tìm thấy lịch hẹn với ID: " + appointmentId));

        boolean hasRecord = medicalRecordRepository.existsByAppointment_Id(appointmentId);
        return AppointmentDTO.fromEntity(a, formatSchedule(a.getWorkingSchedule()), hasRecord);
    }

    private String formatSchedule(WorkingSchedule schedule) {
        if (schedule == null) return "";
        return schedule.getStartTime() + " - " + schedule.getEndTime();
    }

}

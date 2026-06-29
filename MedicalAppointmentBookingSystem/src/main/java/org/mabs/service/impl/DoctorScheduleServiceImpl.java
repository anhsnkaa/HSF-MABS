package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.AppointmentDTO;
import org.mabs.entity.Appointment;
import org.mabs.entity.WorkingSchedule;
import org.mabs.repository.AppointmentRepository;
import org.mabs.service.DoctorScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDate(Long doctorId, LocalDate date, String statusFilter) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23,59,59);

        List<Appointment> appointments = appointmentRepository.findByDoctorAndDateRange(doctorId,startOfDay,endOfDay);
        if(statusFilter != null && !statusFilter.isBlank()){
            appointments = appointments.stream().filter(a -> statusFilter.equals(a.getStatus())).toList();
        }
        return appointments.stream().map(a -> AppointmentDTO.fromEntity(a, formatSchedule(a.getWorkingSchedule()))).toList();
    }

    @Override
    public AppointmentDTO getAppointmentDetail(Long appointmentId) {
         Appointment a = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch hẹn!"));
         return AppointmentDTO.fromEntity(a,formatSchedule(a.getWorkingSchedule()));
    }

    private String formatSchedule(WorkingSchedule schedule) {
        if (schedule == null) return "";
        return schedule.getStartTime() + " - " + schedule.getEndTime();
    }

}

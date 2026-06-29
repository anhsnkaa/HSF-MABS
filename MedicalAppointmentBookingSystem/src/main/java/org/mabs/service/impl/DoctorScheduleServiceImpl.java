package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.AppointmentDTO;
import org.mabs.service.DoctorScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByDate(Long doctorId, LocalDate date, String statusFilter) {
        return List.of();
    }

    @Override
    public AppointmentDTO getAppointmentDetail(Long appointmentId) {
        return null;
    }
}

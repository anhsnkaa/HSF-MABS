package org.mabs.service;

import org.mabs.dto.AppointmentDTO;

import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
    List<AppointmentDTO> getAppointmentsByDate(Long doctorId, LocalDate date, String statusFilter);
    AppointmentDTO getAppointmentDetail(Long appointmentId);
}

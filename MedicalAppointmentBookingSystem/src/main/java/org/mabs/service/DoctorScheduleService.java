package org.mabs.service;

import org.mabs.dto.AppointmentDTO;

import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
    List<AppointmentDTO> getAppointmentsByDate(Long doctorId, LocalDate date, String statusFilter);
    List<AppointmentDTO> getAppointments(Long doctorId, String statusFilter);
    AppointmentDTO getAppointmentDetail(Long appointmentId);
}

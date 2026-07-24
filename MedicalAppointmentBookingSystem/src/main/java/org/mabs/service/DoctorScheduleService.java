package org.mabs.service;

import org.mabs.dto.AppointmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
    List<AppointmentDTO> getAppointmentsByDate(Long doctorId, LocalDate date, String statusFilter);
    List<AppointmentDTO> getAppointments(Long doctorId, String statusFilter);
    Page<AppointmentDTO> getAppointmentsPage(Long doctorId, String statusFilter, Pageable pageable);
    AppointmentDTO getAppointmentDetail(Long appointmentId);
}

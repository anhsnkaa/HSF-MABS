package org.mabs.service;

import org.mabs.dto.BookingRequest;
import org.mabs.dto.CancelRequest;
import org.mabs.dto.RescheduleRequest;
import org.mabs.entity.Appointment;
import org.mabs.entity.WorkingSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<WorkingSchedule> getWorkingSchedules(Long doctorId);
    Appointment findByIdAndPatientId(Long id, Long patientId);
    void bookAppointment(BookingRequest request, Long patientId);
    Page<Appointment> getPatientAppointments(Long patientId, Pageable pageable);
    List<Appointment> getAllPatientAppointments(Long patientId);
    void cancelAppointment(CancelRequest request, Long patientId);
    void rescheduleAppointment(RescheduleRequest request, Long patientId);
}
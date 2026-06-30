package org.mabs.service;

import org.mabs.dto.BookingRequest;
import org.mabs.dto.CancelRequest;
import org.mabs.dto.RescheduleRequest;
import org.mabs.entity.Appointment;
import org.mabs.entity.WorkingSchedule;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<WorkingSchedule> getWorkingSchedules(Long doctorId);
    Appointment findByIdAndPatientId(Long id, Long patientId);
    void bookAppointment(BookingRequest request, Long patientId);
    List<Appointment> getPatientAppointments(Long patientId);
    void cancelAppointment(CancelRequest request, Long patientId);
    void rescheduleAppointment(RescheduleRequest request, Long patientId);
}
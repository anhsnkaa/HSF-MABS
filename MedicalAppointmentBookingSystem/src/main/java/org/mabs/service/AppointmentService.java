package org.mabs.service;

import org.mabs.dto.BookingRequest;
import org.mabs.dto.CancelRequest;
import org.mabs.dto.RescheduleRequest;
import org.mabs.entity.Appointment;
import org.mabs.entity.WorkingSchedule;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AppointmentService {
    List<WorkingSchedule> getWorkingSchedules(Long doctorId);
    void bookAppointment(BookingRequest request, Long patientId);
    List<Appointment> getPatientAppointments(Long patientId);
    void cancelAppointment(CancelRequest request, Long patientId);
    void rescheduleAppointment(RescheduleRequest request, Long patientId);
}
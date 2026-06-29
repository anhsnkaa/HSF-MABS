package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.BookingRequest;
import org.mabs.dto.CancelRequest;
import org.mabs.dto.RescheduleRequest;
import org.mabs.entity.Appointment;
import org.mabs.entity.Doctor;
import org.mabs.entity.User;
import org.mabs.entity.WorkingSchedule;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.UserRepository;
import org.mabs.repository.WorkingScheduleRepository;
import org.mabs.service.AppointmentService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final WorkingScheduleRepository workingScheduleRepository;

    @Override
    public List<WorkingSchedule> getWorkingSchedules(Long doctorId) {
        return workingScheduleRepository.findByDoctorIdAndStatus(doctorId, "open");
    }

    @Override
    public void bookAppointment(BookingRequest request, Long patientId) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));
        WorkingSchedule schedule = workingScheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch làm việc"));

        if (request.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Không thể đặt lịch trong quá khứ");
        }

        boolean exists = appointmentRepository.existsByDoctorIdAndAppointmentTimeAndStatusIn(
                request.getDoctorId(), request.getAppointmentTime(), List.of("pending", "confirmed"));
        if (exists) {
            throw new RuntimeException("Slot này đã có người đặt, vui lòng chọn slot khác");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setWorkingSchedule(schedule);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus("pending");

        try {
            appointmentRepository.save(appointment);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Slot này vừa được đặt bởi người khác, vui lòng chọn lại");
        }
    }

    @Override
    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentTimeDesc(patientId);
    }

    @Override
    public void cancelAppointment(CancelRequest request, Long patientId) {
        Appointment appointment = appointmentRepository.findByIdAndPatientId(request.getAppointmentId(), patientId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if (!List.of("pending", "confirmed").contains(appointment.getStatus())) {
            throw new RuntimeException("Không thể hủy lịch hẹn ở trạng thái này");
        }

        if (appointment.getAppointmentTime().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Chỉ có thể hủy lịch trước 2 tiếng");
        }

        appointment.setStatus("cancelled");
        appointment.setCancelReason(request.getCancelReason());
        appointmentRepository.save(appointment);
    }

    @Override
    public void rescheduleAppointment(RescheduleRequest request, Long patientId) {
        Appointment appointment = appointmentRepository.findByIdAndPatientId(request.getAppointmentId(), patientId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if (!List.of("pending", "confirmed").contains(appointment.getStatus())) {
            throw new RuntimeException("Không thể dời lịch hẹn ở trạng thái này");
        }

        if (appointment.getAppointmentTime().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Chỉ có thể dời lịch trước 2 tiếng");
        }

        WorkingSchedule newSchedule = workingScheduleRepository.findById(request.getNewScheduleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch làm việc"));

        if (request.getNewAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Không thể dời lịch về quá khứ");
        }

        boolean exists = appointmentRepository.existsByDoctorIdAndAppointmentTimeAndStatusIn(
                appointment.getDoctor().getId(), request.getNewAppointmentTime(), List.of("pending", "confirmed"));
        if (exists) {
            throw new RuntimeException("Slot mới đã có người đặt, vui lòng chọn slot khác");
        }

        appointment.setWorkingSchedule(newSchedule);
        appointment.setAppointmentTime(request.getNewAppointmentTime());
        appointmentRepository.save(appointment);
    }
}
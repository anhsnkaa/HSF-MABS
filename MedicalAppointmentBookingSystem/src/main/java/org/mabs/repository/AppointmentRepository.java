package org.mabs.repository;

import org.mabs.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdAndAppointmentTimeAfterAndStatusNotOrderByAppointmentTimeAsc(
            Long patientId,
            LocalDateTime currentTime,
            String status
    );
    @Query("from Appointment ap where ap.patient.id = :id")
    List<Appointment> findByPatientId(@Param("id") Long id);

    boolean existsByDoctorIdAndAppointmentTimeAndStatusIn(Long doctorId, LocalDateTime appointmentTime, List<String> statuses);
    Optional<Appointment> findByIdAndPatientId(Long id, Long patientId);
    List<Appointment> findByPatientIdOrderByAppointmentTimeDesc(Long patientId);
}

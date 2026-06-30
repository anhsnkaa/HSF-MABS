package org.mabs.repository;

import org.mabs.entity.Appointment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @EntityGraph(attributePaths = {"doctor", "doctor.user", "doctor.specialty"})
    List<Appointment> findByPatientIdAndAppointmentTimeAfterAndStatusNotOrderByAppointmentTimeAsc(
            Long patientId,
            LocalDateTime currentTime,
            String status
    );

    @EntityGraph(attributePaths = {"doctor", "doctor.user", "doctor.specialty"})
    Optional<Appointment> findByIdAndPatientId(Long id, Long patientId);

    @EntityGraph(attributePaths = {"doctor", "doctor.user", "doctor.specialty"})
    List<Appointment> findByPatientIdOrderByAppointmentTimeDesc(Long patientId);

    @Query("from Appointment ap where ap.patient.id = :id")
    List<Appointment> findByPatientId(@Param("id") Long id);

    boolean existsByDoctorIdAndAppointmentTimeAndStatusIn(Long doctorId, LocalDateTime appointmentTime, List<String> statuses);
}

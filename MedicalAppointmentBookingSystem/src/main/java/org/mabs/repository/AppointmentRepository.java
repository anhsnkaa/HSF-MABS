package org.mabs.repository;

import org.mabs.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdAndAppointmentTimeAfterAndStatusNotOrderByAppointmentTimeAsc(
            Long patientId,
            LocalDateTime currentTime,
            String status
    );
    @Query("from Appointment ap where ap.patient.id = :id")
    List<Appointment> findByPatientId(@Param("id") Long id);


}

package org.mabs.repository;

import org.mabs.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("from Appointment ap where ap.patient.id = :id")
    List<Appointment> findByPatientId(@Param("id") Long id);


}

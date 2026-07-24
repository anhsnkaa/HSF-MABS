package org.mabs.repository;

import org.mabs.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByDoctorId(Long doctorId);
    boolean existsByAppointmentId(Long appointmentId);
    Optional<Review> findByAppointmentId(Long appointmentId);

    @Query("SELECT r.appointment.id FROM Review r WHERE r.patient.id = :patientId")
    List<Long> findReviewedAppointmentIdsByPatientId(@Param("patientId") Long patientId);
}


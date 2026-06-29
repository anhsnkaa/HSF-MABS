package org.mabs.repository;

import org.mabs.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    @Query("from TestResult tr where tr.appointment.id =:id")
    List<TestResult> findByAppointmentId(@Param("id") Long appointmentId);

    @Query("from TestResult tr where tr.appointment.patient.id =:id order by tr.uploadedAt desc")
    List<TestResult> findByPatientId(@Param("id") Long patientId);
}

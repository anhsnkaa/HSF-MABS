package org.mabs.repository;

import org.mabs.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("from MedicalRecord mr where mr.patient.id =:id order by mr.visitDate desc")
    List<MedicalRecord> findByPatientIdOrderByVisitDate(@Param("id") Long id);

    boolean existsByAppointment_Id(Long appointmentId);

    @Query("select mr from MedicalRecord mr " +
           "left join fetch mr.patient " +
           "left join fetch mr.appointment " +
           "left join fetch mr.doctor " +
           "where mr.id = :id")
    Optional<MedicalRecord> findByIdWithDetails(@Param("id") Long id);

}

package org.mabs.repository;

import org.mabs.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("from MedicalRecord mr where mr.patient.id =:id order by mr.visitDate desc")
    List<MedicalRecord> findByPatientIdOrderByVisitDate(@Param("id") Long id);
}

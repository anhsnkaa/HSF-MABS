package org.mabs.repository;

import org.mabs.dto.PrescriptionDto;
import org.mabs.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query("from Prescription p where p.medicalRecord.id =:id")
    List<Prescription> findByMedicalRecordId(@Param("id") Long id);

}

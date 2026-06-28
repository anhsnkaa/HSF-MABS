package org.mabs.repository;

import org.mabs.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByMedicalRecord_Id(Long id);
}

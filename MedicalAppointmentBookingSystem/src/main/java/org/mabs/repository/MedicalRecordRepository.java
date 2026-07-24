package org.mabs.repository;

import org.mabs.entity.Doctor;
import org.mabs.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("select mr from MedicalRecord mr "
           + "left join fetch mr.doctor d "
           + "left join fetch d.user "
           + "where mr.patient.id = :id order by mr.visitDate desc")
    List<MedicalRecord> findByPatientIdOrderByVisitDate(@Param("id") Long id);

    boolean existsByAppointment_Id(Long appointmentId);

    @Query("select mr from MedicalRecord mr " +
           "left join fetch mr.patient " +
           "left join fetch mr.appointment " +
           "left join fetch mr.doctor " +
           "where mr.id = :id")
    Optional<MedicalRecord> findByIdWithDetails(@Param("id") Long id);

    @Query(
            value = """
                    SELECT mr FROM MedicalRecord mr
                    JOIN FETCH mr.doctor d
                    JOIN FETCH d.user u
                    WHERE mr.patient.id = :patientId
                      AND (:doctorId IS NULL OR d.id = :doctorId)
                      AND (:keyword IS NULL OR :keyword = ''
                           OR LOWER(mr.diagnosis) LIKE LOWER(CONCAT('%', :keyword, '%'))
                           OR LOWER(mr.symptoms) LIKE LOWER(CONCAT('%', :keyword, '%'))
                           OR LOWER(mr.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))
                           OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
                      AND (:fromDate IS NULL OR mr.visitDate >= :fromDate)
                      AND (:toDate IS NULL OR mr.visitDate <= :toDate)
                    ORDER BY mr.visitDate DESC, mr.id DESC
                    """,
            countQuery = """
                    SELECT COUNT(mr) FROM MedicalRecord mr
                    JOIN mr.doctor d
                    JOIN d.user u
                    WHERE mr.patient.id = :patientId
                      AND (:doctorId IS NULL OR d.id = :doctorId)
                      AND (:keyword IS NULL OR :keyword = ''
                           OR LOWER(mr.diagnosis) LIKE LOWER(CONCAT('%', :keyword, '%'))
                           OR LOWER(mr.symptoms) LIKE LOWER(CONCAT('%', :keyword, '%'))
                           OR LOWER(mr.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))
                           OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
                      AND (:fromDate IS NULL OR mr.visitDate >= :fromDate)
                      AND (:toDate IS NULL OR mr.visitDate <= :toDate)
                    """
    )
    Page<MedicalRecord> searchByPatient(
            @Param("patientId") Long patientId,
            @Param("keyword") String keyword,
            @Param("doctorId") Long doctorId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);

    @Query("""
            SELECT DISTINCT d FROM MedicalRecord mr
            JOIN mr.doctor d
            JOIN FETCH d.user
            WHERE mr.patient.id = :patientId
            ORDER BY d.id
            """)
    List<Doctor> findDoctorsByPatientId(@Param("patientId") Long patientId);
}

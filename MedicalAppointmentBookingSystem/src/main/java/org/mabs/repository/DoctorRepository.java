package org.mabs.repository;

import org.mabs.entity.Doctor;
import org.mabs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d JOIN FETCH d.user JOIN FETCH d.specialty")
    List<Doctor> findAllDoctors();

    @Query(value = """
        SELECT d FROM Doctor d
        JOIN FETCH d.user
        JOIN FETCH d.specialty
        WHERE (:specialtyId IS NULL OR d.specialty.id = :specialtyId)
        AND (:keyword IS NULL OR LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """,
        countQuery = """
        SELECT COUNT(d) FROM Doctor d
        WHERE (:specialtyId IS NULL OR d.specialty.id = :specialtyId)
        AND (:keyword IS NULL OR LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """)
    Page<Doctor> searchDoctors(@Param("specialtyId") Long specialtyId,
                               @Param("keyword") String keyword,
                               Pageable pageable);

    List<Doctor> findByUserFullNameContaining(String keyword);
    List<Doctor> findBySpecialtyId(Long specialtyId);
    List<Doctor> findBySpecialtyIdAndUserFullNameContaining(Long specialtyId, String keyword);
    List<Doctor> findBySpecialtyIdOrUserFullNameContainingOrTitleContainingOrBioContaining(
            Long specialtyId, String fullName, String title, String bio);
    boolean existsBySpecialtyId(Long specialtyId);
    Optional<Doctor> findByUserId(Long userId);
}

package org.mabs.repository;

import org.mabs.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByUserFullNameContaining(String keyword);
    List<Doctor> findBySpecialtyId(Long specialtyId);
    List<Doctor> findBySpecialtyIdAndUserFullNameContaining(Long specialtyId, String keyword);
    List<Doctor> findBySpecialtyIdOrUserFullNameContainingOrTitleContainingOrBioContaining(
            Long specialtyId, String fullName, String title, String bio);

    Optional<Doctor> findByUserId(Long userId);
}

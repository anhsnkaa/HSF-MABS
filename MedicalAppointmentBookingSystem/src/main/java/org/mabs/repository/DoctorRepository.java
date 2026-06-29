package org.mabs.repository;

import org.mabs.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d JOIN FETCH d.user JOIN FETCH d.specialty")
    List<Doctor> findAllDoctors();

    List<Doctor> findByUserFullNameContaining(String keyword);
    List<Doctor> findBySpecialtyId(Long specialtyId);
    List<Doctor> findBySpecialtyIdAndUserFullNameContaining(Long specialtyId, String keyword);
    List<Doctor> findBySpecialtyIdOrUserFullNameContainingOrTitleContainingOrBioContaining(
            Long specialtyId, String fullName, String title, String bio);

}

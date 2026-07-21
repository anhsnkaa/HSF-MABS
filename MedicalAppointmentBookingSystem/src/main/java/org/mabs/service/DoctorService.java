package org.mabs.service;

import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.entity.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {
    Page<Doctor> searchDoctors(DoctorSearch doctorSearch, Pageable pageable);

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(Long id);

    Doctor createDoctor(Doctor doctor);

    Doctor updateDoctor(Doctor doctor);

    Doctor findById(Long id);

    boolean existsBySpecialty(Specialty specialty);
}

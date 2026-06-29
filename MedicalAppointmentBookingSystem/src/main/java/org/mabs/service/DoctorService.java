package org.mabs.service;

import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> searchDoctors(DoctorSearch doctorSearch);
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(Long id);
    Doctor createDoctor(Doctor doctor);
}

package org.mabs.service;

import org.mabs.entity.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors();

    Doctor createDoctor(Doctor doctor);
}

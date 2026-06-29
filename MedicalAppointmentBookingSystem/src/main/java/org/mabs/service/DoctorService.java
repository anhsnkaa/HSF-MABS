package org.mabs.service;

import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DoctorService {
    List<Doctor> searchDoctors(DoctorSearch doctorSearch);
}

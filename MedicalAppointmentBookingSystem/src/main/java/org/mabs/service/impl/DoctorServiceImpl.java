package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.entity.Doctor;
import org.mabs.repository.DoctorRepository;
import org.mabs.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository repository;

    @Override
    public List<Doctor> getAllDoctors() {
        return repository.findAllDoctors();
    }
}

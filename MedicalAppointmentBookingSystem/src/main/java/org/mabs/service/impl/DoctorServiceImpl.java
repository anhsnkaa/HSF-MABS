package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.repository.DoctorRepository;
import org.mabs.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repository;

    @Override
    public List<Doctor> getAllDoctors() {
        return repository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return repository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        return repository.save(doctor);
    }

    public Doctor findById(Long id) {
        return repository.findById(id).orElse(null);
    }


    @Override
    public List<Doctor> searchDoctors(DoctorSearch search) {
        List<Doctor> doctors = repository.findAll();

        List<Doctor> doctors;
        if (hasSpecialty && hasKeyword) {
            doctors = repository.findBySpecialtyIdAndUserFullNameContaining(search.getSpecialtyId(), search.getKeyword().trim());
        } else if (hasSpecialty) {
            doctors = repository.findBySpecialtyId(search.getSpecialtyId());
        } else if (hasKeyword) {
            doctors = repository.findByUserFullNameContaining(search.getKeyword().trim());
        } else {
            doctors = repository.findAll();
        }

        if (search.getSortBy() != null) {
            if (search.getSortBy().equals("experience")) {
                doctors.sort(Comparator.comparing(Doctor::getExperienceYears, Comparator.reverseOrder()));
            } else if (search.getSortBy().equals("fee")) {
                doctors.sort(Comparator.comparing(Doctor::getConsultationFee));
            }
        }

        return doctors;
    }
        }

package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.entity.Specialty;
import org.mabs.entity.User;
import org.mabs.exception.ResourceNotFoundException;
import org.mabs.repository.DoctorRepository;
import org.mabs.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        if (search.getSpecialtyId() != null) {
            List<Doctor> specialtyFiltered = new ArrayList<>();
            for (Doctor doctor : doctors) {
                if (doctor.getSpecialty().getId().equals(search.getSpecialtyId())) {
                    specialtyFiltered.add(doctor);
                }
            }
            doctors = specialtyFiltered;
        }

        if (search.getKeyword() != null && !search.getKeyword().trim().isEmpty()) {
            String keyword = search.getKeyword().toLowerCase().trim();
            List<Doctor> keywordFiltered = new ArrayList<>();
            for (Doctor doctor : doctors) {
                if (doctor.getUser().getFullName().toLowerCase().contains(keyword)) {
                    keywordFiltered.add(doctor);
                }
            }
            doctors = keywordFiltered;
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

    @Override
    public Doctor findByUserId(Long id) {
        return repository.findByUserId(id).orElse(null);
    }

    @Override
    public boolean existsBySpecialty(Specialty specialty) {
        return repository.existsBySpecialtyId(specialty.getId());
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bác sĩ"));
        repository.deleteById(id);
    }
}

package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.entity.Specialty;
import org.mabs.repository.DoctorRepository;
import org.mabs.service.DoctorService;
import org.mabs.specification.DoctorSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    public Page<Doctor> searchDoctors(DoctorSearch search, Pageable pageable) {
        Specification<Doctor> spec = DoctorSpecification.from(search);

        if ("experience".equals(search.getSortBy())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "experienceYears"));
        } else if ("fee".equals(search.getSortBy())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "consultationFee"));
        }

        return repository.findAll(spec, pageable);
    }

    @Override
    public boolean existsBySpecialty(Specialty specialty) {
        return repository.existsBySpecialtyId(specialty.getId());
    }
}

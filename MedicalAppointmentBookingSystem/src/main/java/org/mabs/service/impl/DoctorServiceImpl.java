package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.entity.Specialty;
import org.mabs.entity.User;
import org.mabs.exception.ResourceNotFoundException;
import org.mabs.repository.DoctorRepository;
import org.mabs.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Doctor> searchDoctors(DoctorSearch search, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "experienceYears");
        if (search.getSortBy() != null) {
            if (search.getSortBy().equalsIgnoreCase("experience")) {
                sort = Sort.by(Sort.Direction.DESC, "experienceYears");
            } else if (search.getSortBy().equalsIgnoreCase("fee")) {
                sort = Sort.by(Sort.Direction.ASC, "consultationFee");
            }
        }

        String keyword = (search.getKeyword() != null && !search.getKeyword().trim().isEmpty())
                ? search.getKeyword().trim() : null;

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.searchDoctors(search.getSpecialtyId(), keyword, sortedPageable);
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

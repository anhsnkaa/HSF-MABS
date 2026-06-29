package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.repository.DoctorRepository;
import org.mabs.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public List<Doctor> searchDoctors(DoctorSearch search) {
        List<Doctor> doctors = doctorRepository.findAll();

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

        if (search.getMinRating() != null && search.getMinRating() > 0) {
            List<Doctor> ratingFiltered = new ArrayList<>();
            for (Doctor doctor : doctors) {
                if (doctor.getRatingAvg() != null
                        && doctor.getRatingAvg().doubleValue() >= search.getMinRating()) {
                    ratingFiltered.add(doctor);
                }
            }
            doctors = ratingFiltered;
        }

        if (search.getSortBy() != null) {
            if (search.getSortBy().equals("rating")) {
                doctors.sort(new Comparator<Doctor>() {
                    public int compare(Doctor doctor1, Doctor doctor2) {
                        return doctor2.getRatingAvg().compareTo(doctor1.getRatingAvg());
                    }
                });
            } else if (search.getSortBy().equals("experience")) {
                doctors.sort(new Comparator<Doctor>() {
                    public int compare(Doctor doctor1, Doctor doctor2) {
                        return doctor2.getExperienceYears().compareTo(doctor1.getExperienceYears());
                    }
                });
            } else if (search.getSortBy().equals("fee")) {
                doctors.sort(new Comparator<Doctor>() {
                    public int compare(Doctor doctor1, Doctor doctor2) {
                        return doctor1.getConsultationFee().compareTo(doctor2.getConsultationFee());
                    }
                });
            }
        }

        return doctors;
    }
}
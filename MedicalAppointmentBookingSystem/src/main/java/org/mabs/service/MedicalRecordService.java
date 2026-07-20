package org.mabs.service;

import org.mabs.dto.MedicalRecordDto;
import org.mabs.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordDto> getMedicalRecordsByPatient(Long patientId);

    Page<MedicalRecordDto> searchByPatient(Long patientId, String keyword, Long doctorId,
                                           LocalDate fromDate, LocalDate toDate, Pageable pageable);

    List<Doctor> getDoctorsByPatient(Long patientId);

    MedicalRecordDto createRecord(Long appointmentId, Long doctorId, String symptoms, String diagnosis, String notes);
}

package org.mabs.service;

import org.mabs.dto.MedicalRecordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordDto> getMedicalRecordsByPatient(Long patientId);

    Page<MedicalRecordDto> getMedicalRecordsByPatientPage(Long patientId, Pageable pageable);

    MedicalRecordDto createRecord(Long appointmentId, Long doctorId, String symptoms, String diagnosis, String notes);
}

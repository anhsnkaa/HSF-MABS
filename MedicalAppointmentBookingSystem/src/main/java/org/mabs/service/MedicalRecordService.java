package org.mabs.service;

import org.mabs.dto.MedicalRecordDto;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordDto> getMedicalRecordsByPatient(Long patientId);

    MedicalRecordDto createRecord(Long appointmentId, Long doctorId, String symptoms, String diagnosis, String notes);
}

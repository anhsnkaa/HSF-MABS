package org.mabs.service;

import org.mabs.dto.MedicalRecordDto;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordDto> getMedicalRecordsByPatient(Long patientId);
}

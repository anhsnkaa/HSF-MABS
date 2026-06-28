package org.mabs.service;

import org.mabs.dto.MedicalRecordResponse;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordResponse> getMedicalRecordsByPatient(Integer patientId);
}

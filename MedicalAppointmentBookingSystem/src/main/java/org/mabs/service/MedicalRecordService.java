package org.mabs.service;

import org.mabs.dto.MedicalRecordRequest;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordRequest> getMedicalRecordsByPatient(Integer patientId);
}

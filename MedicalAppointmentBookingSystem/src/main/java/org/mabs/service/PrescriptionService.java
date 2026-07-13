package org.mabs.service;

import org.mabs.dto.PrescriptionDto;
import org.mabs.dto.PrescriptionRequestDto;

import java.util.List;

public interface PrescriptionService {
    PrescriptionDto addPrescription(PrescriptionRequestDto dto, Long currentDoctorId);
    List<PrescriptionDto> getPrescriptionsByRecordId(Long medicalRecordId);
    void removePrescription(Long prescriptionId, Long currentDoctorId);
}

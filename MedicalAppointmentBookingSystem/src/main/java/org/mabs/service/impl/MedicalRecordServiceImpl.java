package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.dto.PrescriptionDto;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.Medicine;
import org.mabs.entity.Prescription;
import org.mabs.repository.MedicalRecordRepository;
import org.mabs.repository.PrescriptionRepository;
import org.mabs.service.MedicalRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getMedicalRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByVisitDate(patientId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private MedicalRecordDto toDto(MedicalRecord mr) {
        List<PrescriptionDto> prescriptionDtos = prescriptionRepository
                .findByMedicalRecordId(mr.getId())
                .stream()
                .map(this::toPrescriptionDto)
                .toList();
        return new MedicalRecordDto(
                mr.getId(), mr.getSymptoms(), mr.getDiagnosis(),
                mr.getNotes(), mr.getVisitDate(), prescriptionDtos
        );
    }

    private PrescriptionDto toPrescriptionDto(Prescription p) {
        Medicine medicine = p.getMedicine();
        String medicineName = (medicine != null) ? medicine.getName() : "Medicine name not available !";
        String unit = (medicine != null) ? medicine.getUnit() : "Unit name not available !";
        return new PrescriptionDto(
                medicineName, unit, p.getQuantity(), p.getDosage(),
                p.getFrequency(), p.getDurationDays(), p.getNote()
        );
    }
}

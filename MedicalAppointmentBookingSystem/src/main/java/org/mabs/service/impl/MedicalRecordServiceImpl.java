package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordResponse;
import org.mabs.dto.PrescriptionResponse;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.Medicine;
import org.mabs.entity.Prescription;
import org.mabs.repository.MedicalRecordRepository;
import org.mabs.repository.PrescriptionRepository;
import org.mabs.service.MedicalRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getMedicalRecordsByPatient(Long patientId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientIdOrderByVisitDate(patientId);
        List<MedicalRecordResponse> results = new ArrayList<>();

        for (MedicalRecord record : records) {
            List<Prescription> prescriptions = prescriptionRepository.findByMedicalRecord_Id(record.getId());
            List<PrescriptionResponse> prescriptionResponses = new ArrayList<>();
            for (Prescription p : prescriptions) {
                Medicine medicine = p.getMedicine();
                String medicineName = (medicine != null) ? medicine.getName() : "Medicine name not available !";
                String unit = (medicine != null) ? medicine.getUnit() : "Medicine unit not available !";
                prescriptionResponses.add(new PrescriptionResponse(
                        medicineName, unit, p.getQuantity(), p.getDosage(),
                        p.getFrequency(), p.getDurationDays(), p.getNote()
                ));

            }

            MedicalRecordResponse dto = new MedicalRecordResponse(
                    record.getId(),
                    record.getSymptoms(),
                    record.getDiagnosis(),
                    record.getNotes(),
                    record.getVisitDate(),
                    prescriptionResponses
            );
            results.add(dto);
        }

        return results;
    }
}

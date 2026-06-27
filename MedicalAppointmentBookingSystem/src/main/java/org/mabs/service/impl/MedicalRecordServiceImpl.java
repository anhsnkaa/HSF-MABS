package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordRequest;
import org.mabs.dto.PrescriptionRequest;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.Medicine;
import org.mabs.entity.Prescription;
import org.mabs.repository.MedicalRecordRepository;
import org.mabs.repository.MedicineRepository;
import org.mabs.repository.PrescriptionRepository;
import org.mabs.service.MedicalRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicineRepository medicineRepository;

    @Override
    public List<MedicalRecordRequest> getMedicalRecordsByPatient(Integer patientId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientIdOrderByVisitDate(patientId);
        List<MedicalRecordRequest> results = new ArrayList<>();

        for (MedicalRecord record : records) {
            List<Prescription> prescriptions = prescriptionRepository.findByMedicalRecordId(record.getId());
            List<PrescriptionRequest> prescriptionRequests = new ArrayList<>();
            for (Prescription p : prescriptions) {
                Medicine medicine = medicineRepository.findById(p.getMedicineId()).orElse(null);
                String medicineName = (medicine != null) ? medicine.getName() : "Medicine name not available !";
                String unit = (medicine != null) ? medicine.getUnit() : "Medicine unit not available !";
                prescriptionRequests.add(new PrescriptionRequest(
                        medicineName, unit, p.getQuantity(), p.getDosage(),
                        p.getFrequency(), p.getDurationDays(), p.getNote()
                ));

            }

            MedicalRecordRequest dto = new MedicalRecordRequest(
                    record.getId(),
                    record.getSymptoms(),
                    record.getDiagnosis(),
                    record.getNotes(),
                    record.getVisitDate(),
                    prescriptionRequests
            );
            results.add(dto);
        }

        return results;
    }
}

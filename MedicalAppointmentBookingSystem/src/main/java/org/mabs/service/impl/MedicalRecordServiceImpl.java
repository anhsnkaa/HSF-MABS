package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.dto.PrescriptionDto;
import org.mabs.entity.Appointment;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.Medicine;
import org.mabs.entity.Prescription;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.MedicalRecordRepository;
import org.mabs.repository.PrescriptionRepository;
import org.mabs.service.MedicalRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;




    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getMedicalRecordsByPatient(Long patientId) {
        List<MedicalRecord> entities = medicalRecordRepository.findByPatientIdOrderByVisitDate(patientId);
        List<MedicalRecordDto> dtos = new ArrayList<>();
        for (MedicalRecord mr : entities) {
            dtos.add(this.toDto(mr));
        }
        return dtos;
    }

    private MedicalRecordDto toDto(MedicalRecord mr) {
        List<Prescription> prescriptions = prescriptionRepository.findByMedicalRecordId(mr.getId());
        List<PrescriptionDto> prescriptionDtos = new ArrayList<>();
        for (Prescription p : prescriptions) {
            prescriptionDtos.add(this.toPrescriptionDto(p));
        }
        String doctorName = (mr.getDoctor() != null && mr.getDoctor().getUser() != null)
                ? mr.getDoctor().getUser().getFullName()
                : "N/A";
        return new MedicalRecordDto(
                mr.getId(), mr.getSymptoms(), mr.getDiagnosis(),
                mr.getNotes(), doctorName, mr.getVisitDate(), prescriptionDtos
        );
    }

    private PrescriptionDto toPrescriptionDto(Prescription p) {
        Medicine medicine = p.getMedicine();
        String medicineName = (medicine != null) ? medicine.getName() : "Medicine name not available !";
        String unit = (medicine != null) ? medicine.getUnit() : "Unit name not available !";
        return new PrescriptionDto(
                p.getId(),
                medicineName, unit, p.getQuantity(), p.getDosage(),
                p.getFrequency(), p.getDurationDays(), p.getNote()
        );
    }
}
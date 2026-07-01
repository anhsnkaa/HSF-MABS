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


    @Override
    @Transactional
    public MedicalRecordDto createRecord(Long appointmentId, Long doctorId,
                                         String symptoms, String diagnosis, String notes) {
        // 1. Check duplicate
        if (medicalRecordRepository.existsByAppointment_Id(appointmentId)) {
            throw new IllegalStateException(
                    "Hồ sơ bệnh án cho lịch hẹn này đã tồn tại");
        }

        // 2. Load appointment
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy lịch hẹn với ID: " + appointmentId));

        // 3. Create MedicalRecord
        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appt);
        record.setDoctor(appt.getDoctor());
        record.setPatient(appt.getPatient());
        record.setSymptoms(symptoms);
        record.setDiagnosis(diagnosis);
        record.setNotes(notes);
        record.setVisitDate(LocalDate.now());
        record.setCreatedAt(LocalDateTime.now());
        MedicalRecord saved = medicalRecordRepository.save(record);

        appt.setStatus("completed");
        appt.setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(appt);

        return new MedicalRecordDto(
                saved.getId(),
                saved.getSymptoms(),
                saved.getDiagnosis(),
                saved.getNotes(),
                saved.getVisitDate(),
                new ArrayList<>()
        );
    }

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

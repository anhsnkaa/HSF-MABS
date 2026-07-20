package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.MedicalRecordDto;
import org.mabs.dto.PrescriptionDto;
import org.mabs.entity.Appointment;
import org.mabs.entity.Doctor;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.Medicine;
import org.mabs.entity.Prescription;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.MedicalRecordRepository;
import org.mabs.repository.PrescriptionRepository;
import org.mabs.service.MedicalRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

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

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalRecordDto> searchByPatient(Long patientId, String keyword, Long doctorId,
                                                  LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        String normalizedKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;

        Page<MedicalRecord> page = medicalRecordRepository.searchByPatient(
                patientId, normalizedKeyword, doctorId, fromDate, toDate, pageable);

        return page.map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsByPatient(Long patientId) {
        return medicalRecordRepository.findDoctorsByPatientId(patientId);
    }

    @Override
    @Transactional
    public MedicalRecordDto createRecord(Long appointmentId, Long doctorId, String symptoms, String diagnosis, String notes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch hẹn"));

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new IllegalArgumentException("Lịch hẹn không thuộc về bác sĩ này");
        }

        if (medicalRecordRepository.existsByAppointment_Id(appointmentId)) {
            throw new IllegalStateException("Hồ sơ khám cho lịch hẹn này đã được tạo");
        }

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDoctor(appointment.getDoctor());
        record.setPatient(appointment.getPatient());
        record.setSymptoms(symptoms);
        record.setDiagnosis(diagnosis);
        record.setNotes(notes);
        record.setVisitDate(appointment.getAppointmentTime().toLocalDate());

        MedicalRecord saved = medicalRecordRepository.save(record);

        appointment.setStatus("completed");
        appointmentRepository.save(appointment);

        return toDto(saved);
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

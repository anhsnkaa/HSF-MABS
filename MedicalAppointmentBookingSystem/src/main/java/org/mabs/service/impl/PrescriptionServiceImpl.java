package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.PrescriptionDto;
import org.mabs.dto.PrescriptionRequestDto;
import org.mabs.entity.MedicalRecord;
import org.mabs.entity.Medicine;
import org.mabs.entity.Prescription;
import org.mabs.repository.MedicalRecordRepository;
import org.mabs.repository.MedicineRepository;
import org.mabs.repository.PrescriptionRepository;
import org.mabs.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepo;
    private final MedicalRecordRepository medicalRecordRepo;
    private final MedicineRepository medicineRepo;

    @Override
    @Transactional
    public PrescriptionDto addPrescription(PrescriptionRequestDto dto, Long doctorId) {
        // 1. Validate cơ bản
        if (dto.getMedicineId() == null)
            throw new IllegalArgumentException("Vui lòng chọn thuốc");
        if (dto.getQuantity() == null || dto.getQuantity() <= 0)
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        if (dto.getDurationDays() == null || dto.getDurationDays() <= 0)
            throw new IllegalArgumentException("Số ngày phải lớn hơn 0");
        if (dto.getDosage() == null || dto.getDosage().isBlank())
            throw new IllegalArgumentException("Vui lòng nhập liều dùng");
        if (dto.getFrequency() == null || dto.getFrequency().isBlank())
            throw new IllegalArgumentException("Vui lòng nhập tần suất");

        // 2. Load MedicalRecord (lazy fields OK vì @Transactional)
        Optional<MedicalRecord> recordOpt = medicalRecordRepo.findById(dto.getMedicalRecordId());
        MedicalRecord record = recordOpt.orElse(null);
        if (record == null) {
            throw new IllegalArgumentException("Không tìm thấy hồ sơ với ID: " + dto.getMedicalRecordId());
        }

        // 3. Auth check
        if (!record.getDoctor().getId().equals(doctorId))
            throw new IllegalStateException("Bạn không có quyền kê đơn cho hồ sơ này");

        // 4. Load Medicine
        Optional<Medicine> medicineOpt = medicineRepo.findById(dto.getMedicineId());
        Medicine medicine = medicineOpt.orElse(null);
        if (medicine == null) {
            throw new IllegalArgumentException("Không tìm thấy thuốc");
        }
        if (!Boolean.TRUE.equals(medicine.getIsActive()))
            throw new IllegalArgumentException("Thuốc này hiện không khả dụng");

        // 5. Tạo Prescription
        Prescription p = new Prescription();
        p.setMedicalRecord(record);
        p.setMedicine(medicine);
        p.setQuantity(dto.getQuantity());
        p.setDosage(dto.getDosage());
        p.setFrequency(dto.getFrequency());
        p.setDurationDays(dto.getDurationDays());
        p.setNote(dto.getNote());

        Prescription saved = prescriptionRepo.save(p);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionDto> getPrescriptionsByRecordId(Long recordId) {
        List<Prescription> prescriptions = prescriptionRepo.findByMedicalRecordId(recordId);
        List<PrescriptionDto> dtos = new ArrayList<>();
        for (Prescription p : prescriptions) {
            dtos.add(this.toDto(p));
        }
        return dtos;
    }

    @Override
    @Transactional
    public void removePrescription(Long prescriptionId, Long doctorId) {
        Optional<Prescription> pOpt = prescriptionRepo.findById(prescriptionId);
        Prescription p = pOpt.orElse(null);
        if (p == null) {
            throw new IllegalArgumentException("Không tìm thấy thuốc trong đơn");
        }
        if (!p.getMedicalRecord().getDoctor().getId().equals(doctorId))
            throw new IllegalStateException("Bạn không có quyền xoá thuốc này");
        prescriptionRepo.delete(p);
    }

    private PrescriptionDto toDto(Prescription p) {
        Medicine m = p.getMedicine();
        return new PrescriptionDto(
                p.getId(),
                m != null ? m.getName() : "?",
                m != null ? m.getUnit() : "",
                p.getQuantity(), p.getDosage(),
                p.getFrequency(), p.getDurationDays(), p.getNote()
        );
    }

}

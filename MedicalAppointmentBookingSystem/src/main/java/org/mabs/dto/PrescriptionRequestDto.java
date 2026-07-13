package org.mabs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionRequestDto {
    private Long medicalRecordId;
    private Long medicineId;
    private Integer quantity;
    private String dosage;
    private String frequency;
    private Integer durationDays;
    private String note;
}

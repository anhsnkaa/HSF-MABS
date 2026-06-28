package org.mabs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionResponse {
    private String medicineName;
    private String unit;
    private Integer quantity;
    private String dosage;
    private String frequency;
    private Integer durationDays;
    private String note;
}

package org.mabs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDto {
    private Long id;
    private String symptom;
    private String diagnosis;
    private String note;
    private LocalDate visitDate;
    private List<PrescriptionDto> prescriptions;
}

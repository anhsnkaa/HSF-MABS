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
public class MedicalRecordResponse {
    private Long id;
    private String symptom;
    private String diagnosis;
    private String note;
    private LocalDate visitDate;
    private List<PrescriptionResponse> prescriptions;
    
    // Additional fields for Doctor information and Review status
    private Long appointmentId;
    private Long doctorId;
    private String doctorName;
    private boolean hasReview;
    private Integer reviewRating;
    private String reviewComment;
}

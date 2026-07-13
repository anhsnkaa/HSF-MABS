package org.mabs.dto;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DoctorRecordFormDto {
    private Long appointmentId;
    private String symptoms;
    private String diagnosis;
    private String notes;
}

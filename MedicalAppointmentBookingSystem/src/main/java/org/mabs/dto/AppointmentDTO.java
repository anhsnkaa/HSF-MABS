package org.mabs.dto;

import lombok.*;
import org.mabs.entity.Appointment;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private Long id;
    private LocalDateTime appointmentTime;
    private String status;
    private String reason;

    private Long patientId;
    private String patientName;
    private String patientPhone;
    private String patientEmail;

    private String doctorName;
    private String doctorTitle;
    private String slotTimeRange;

    public static AppointmentDTO fromEntity(Appointment a, String slotTimeRange) {
        String doctorTitle = a.getDoctor() != null ? a.getDoctor().getTitle() : "";
        String doctorName = a.getDoctor() != null && a.getDoctor().getUser() != null ? a.getDoctor().getUser().getFullName() : "";
        String patientName = a.getPatient() != null ? a.getPatient().getFullName() : "";
        String patientPhone = a.getPatient() != null ? a.getPatient().getPhone() : "";
        String patientEmail = a.getPatient() != null ? a.getPatient().getEmail() : "";
        Long patientId = a.getPatient() != null ? a.getPatient().getId() : null;
        return AppointmentDTO.builder().id(a.getId()).appointmentTime(a.getAppointmentTime()).status(a.getStatus()).reason(a.getReason()).slotTimeRange(slotTimeRange).doctorTitle(doctorTitle).doctorName(doctorName).patientName(patientName).patientPhone(patientPhone).patientEmail(patientEmail).patientId(patientId).build();
    }


}

package org.mabs.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class RescheduleRequest {
    private Long appointmentId;
    private Long newScheduleId;
    private LocalDateTime newAppointmentTime;
}
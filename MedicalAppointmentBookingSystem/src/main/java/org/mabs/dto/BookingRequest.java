package org.mabs.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequest {
    private Long doctorId;
    private Long scheduleId;
    private LocalDateTime appointmentTime;
    private String reason;
}

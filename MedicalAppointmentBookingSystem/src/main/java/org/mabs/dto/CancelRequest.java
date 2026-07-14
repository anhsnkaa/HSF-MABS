package org.mabs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelRequest {
    private Long appointmentId;
    private String cancelReason;
}
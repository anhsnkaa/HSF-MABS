package org.mabs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long appointmentId;
    private Long doctorId;
    private Integer rating; // 1-5 sao
    private String comment;
}

package org.mabs.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    @NotNull(message = "Lịch hẹn không được để trống!")
    private Long appointmentId;

    private Long doctorId;

    @NotNull(message = "Số sao đánh giá không được để trống!")
    @Min(value = 1, message = "Đánh giá tối thiểu là 1 sao!")
    @Max(value = 5, message = "Đánh giá tối đa là 5 sao!")
    private Integer rating; // 1-5 sao

    private String comment;
}


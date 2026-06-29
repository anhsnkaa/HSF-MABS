package org.mabs.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DoctorCreationDto {

    @NotNull(message = "Vui lòng chọn người dùng")
    private Long userId;

    @NotNull(message = "Vui lòng chọn người dùng")
    private Long specialtyId;

    private String title;

    private String bio;

    private BigDecimal consultationFee;

    private Integer experienceYears;
}

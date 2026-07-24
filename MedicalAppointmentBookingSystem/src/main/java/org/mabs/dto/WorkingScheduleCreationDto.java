package org.mabs.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class WorkingScheduleCreationDto {

    @NotNull(message = "Vui lòng chọn bác sĩ")
    private Long doctorId;

    @NotNull(message = "Vui lòng chọn ngày làm việc")
    private LocalDate workDate;

    @NotNull(message = "Vui lòng chọn giờ bắt đầu")
    private LocalTime startTime;

    @NotNull(message = "Vui lòng chọn giờ kết thúc")
    private LocalTime endTime;

    @Min(value = 1, message = "Slot minutes phải > 0")
    private Integer slotMinutes = 30;

    @Pattern(regexp = "open|cancelled|full", message = "Status không hợp lệ")
    private String status = "open";
}

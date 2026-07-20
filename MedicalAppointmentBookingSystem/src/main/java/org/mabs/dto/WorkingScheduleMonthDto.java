package org.mabs.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class WorkingScheduleMonthDto {

    @NotNull(message = "Vui lòng chọn bác sĩ")
    private Long doctorId;

    @NotNull(message = "Vui lòng chọn năm")
    @Min(value = 2020, message = "Năm không hợp lệ")
    @Max(value = 2100, message = "Năm không hợp lệ")
    private Integer year;

    @NotNull(message = "Vui lòng chọn tháng")
    @Min(value = 1, message = "Tháng phải từ 1 đến 12")
    @Max(value = 12, message = "Tháng phải từ 1 đến 12")
    private Integer month;

    @NotNull(message = "Vui lòng chọn giờ bắt đầu")
    private LocalTime startTime;

    @NotNull(message = "Vui lòng chọn giờ kết thúc")
    private LocalTime endTime;

    @Min(value = 1, message = "Slot minutes phải > 0")
    private Integer slotMinutes = 30;

    @Pattern(regexp = "open|cancelled|full", message = "Status không hợp lệ")
    private String status = "open";
}

package org.mabs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialtyCreationDto {

    @NotBlank(message = "Tên chuyên khoa không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;
}

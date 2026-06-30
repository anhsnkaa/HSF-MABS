package org.mabs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccountCreationDto {
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    private String email;

    @Pattern(regexp = "|0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @Pattern(regexp = "admin|doctor|patient")
    private String role = "patient";

    @Pattern(regexp = "active|inactive|locked")
    private String status = "active";

    @Pattern(regexp = "male|female|other")
    private String gender = "other";

    private LocalDate dateOfBirth;

    private String address;

    private String avatarUrl;
}

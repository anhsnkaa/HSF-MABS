package org.mabs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AccountUpdateDto {

    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @Pattern(regexp = "|0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số")
    private String phone;

    private String password;

    @Pattern(regexp = "admin|doctor|patient")
    private String role = "patient";

    @Pattern(regexp = "active|inactive|locked")
    private String status = "active";

    @Pattern(regexp = "male|female|other")
    private String gender = "other";

    private LocalDate dateOfBirth;

    private String address;
}

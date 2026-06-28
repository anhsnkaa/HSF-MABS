package org.mabs.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UserProfileUpdateDto {

    // Cho phép người dùng đổi lại tên nếu lúc đăng ký lỡ gõ sai
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không vượt quá 150 ký tự")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải bắt đầu bằng số 0 và có 10 chữ số")
    private String phone;

    // Giới tính: Phải khớp với CHECK CONSTRAINT trong Database của bạn
    @NotBlank(message = "Vui lòng chọn giới tính")
    @Pattern(regexp = "^(male|female|other)$", message = "Giới tính không hợp lệ")
    private String gender;

    // Ngày sinh: Phải là một ngày trong quá khứ
    @Past(message = "Ngày sinh phải là một ngày trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Spring Boot hiểu input type="date" từ HTML
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "Địa chỉ quá dài")
    private String address;

    private String avatarUrl;
}

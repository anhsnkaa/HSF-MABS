package org.mabs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BIGINT IDENTITY SQL

    private String fullName;
    private String email;
    private String phone;
    private String passwordHash;
    private String role;
    private String status;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String avatarUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Tự động gán thời gian lúc mới tạo tài khoản
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "active";
        }
    }

    // Tự động cập nhật thời gian khi có chỉnh sửa profile
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

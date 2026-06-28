package org.mabs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private WorkingSchedule workingSchedule;

    private LocalDateTime appointmentTime;
    private String status;
    @Column(columnDefinition = "nvarchar(500)")
    private String reason;
    @Column(columnDefinition = "nvarchar(500)")
    private String cancelReason;
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

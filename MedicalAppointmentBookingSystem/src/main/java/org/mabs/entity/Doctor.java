package org.mabs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "specialty"})
@Entity
@Table(name = "doctor")
public class Doctor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "bio", columnDefinition = "nvarchar(max)")
    private String bio;

    @Column(name = "consultation_fee", precision = 12, scale = 0)
    private BigDecimal consultationFee;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @PrePersist
    protected void onCreate() {
        if (this.consultationFee == null) this.consultationFee = BigDecimal.ZERO;
        if (this.experienceYears == null) this.experienceYears = 0;
    }
}
package org.mabs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"user", "speciality"})
@Entity
@Table(name = "doctor")
public class Doctor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "bio", columnDefinition = "nvarchar(max)")
    private String bio;

    @Column(name = "cunsultation_fee", precision = 12, scale = 0, nullable = false)
    private BigDecimal consultationFee;

    @Column(name = "rating_avg", precision = 3, scale = 2,nullable = false)
    private BigDecimal ratingAvg;

    @Column(name = "rating_count",nullable = false)
    private Integer ratingCount;

    @Column(name = "experience_years",nullable = false)
    private Integer experienceYears;
}
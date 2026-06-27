package org.mabs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "medical_record")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "appointment_id")
    private Integer appointmentId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(columnDefinition = "nvarchar(max)")
    private String symptoms;

    @Column(columnDefinition = "nvarchar(max)")
    private String diagnosis;

    @Column(columnDefinition = "nvarchar(max)")
    private String notes;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

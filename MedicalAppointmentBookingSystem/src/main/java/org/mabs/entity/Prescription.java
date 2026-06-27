package org.mabs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "medical_record_id")
    private Integer medicalRecordId;

    @Column(name = "medicine_id")
    private Integer medicineId;

    private Integer quantity;

    @Column(length = 100, nullable = false)
    private String dosage;

    @Column(length = 100, nullable = false)
    private String frequency;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(length = 255)
    private String note;


}

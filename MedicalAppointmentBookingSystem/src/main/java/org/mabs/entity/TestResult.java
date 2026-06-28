package org.mabs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_result")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "appointment_id")
    private Integer appointmentId;


}

package org.mabs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "working_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "start_time", nullable = false, columnDefinition = "time")
    @JdbcTypeCode(SqlTypes.TIME)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false, columnDefinition = "time")
    @JdbcTypeCode(SqlTypes.TIME)
    private LocalTime endTime;

    @Column(name = "slot_minutes")
    private Integer slotMinutes;

    private String status;
}

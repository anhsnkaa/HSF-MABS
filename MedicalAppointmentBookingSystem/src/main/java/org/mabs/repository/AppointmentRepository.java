package org.mabs.repository;

import org.mabs.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdAndAppointmentTimeAfterAndStatusNotOrderByAppointmentTimeAsc( Long patientId, LocalDateTime currentTime, String status);

    Optional<Appointment> findByIdAndPatientId(Long id, Long patientId);

    List<Appointment> findByPatientIdOrderByAppointmentTimeDesc(Long patientId);

    List<Appointment> findByPatientId(@Param("id") Long id);

    @Query(value = """
        SELECT a FROM Appointment a
        JOIN FETCH a.doctor d
        JOIN FETCH d.user
        JOIN FETCH d.specialty
        WHERE a.patient.id = :patientId
        ORDER BY a.appointmentTime DESC
        """,
        countQuery = "SELECT COUNT(a) FROM Appointment a WHERE a.patient.id = :patientId")
    Page<Appointment> findPatientAppointmentsPaged(@Param("patientId") Long patientId, Pageable pageable);

    boolean existsByDoctorIdAndAppointmentTimeAndStatusIn(Long doctorId, LocalDateTime appointmentTime, List<String> statuses);

    @Query("Select count (a) > 0 from Appointment a where a.workingSchedule.id = :id ")
    boolean existsByWorkingScheduleId(@Param("id") Long id);

    @Query("""
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
            and a.appointmentTime BETWEEN :start and :end
                order by a.appointmentTime asc
    """)
    List<Appointment> findByDoctorAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
            );

    List<Appointment> findByDoctorIdOrderByAppointmentTimeAsc(Long doctorId);

    @Query("""
    from Appointment a
    where a.patient.id = :patientId
    and a.status = 'completed'
    order by a.appointmentTime desc
""")
    List<Appointment> findCompletedByPatient(@Param("patientId") Long patientId);

}

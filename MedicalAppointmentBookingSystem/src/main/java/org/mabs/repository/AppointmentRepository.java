package org.mabs.repository;

import org.mabs.entity.Appointment;
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

    boolean existsByDoctorIdAndAppointmentTimeAndStatusIn(Long doctorId, LocalDateTime appointmentTime, List<String> statuses);


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

    @Query("""
    from Appointment a
    where a.patient.id = :patientId
    and a.status = 'completed'
    order by a.appointmentTime desc
""")
    List<Appointment> findCompletedByPatient(@Param("patientId") Long patientId);

}

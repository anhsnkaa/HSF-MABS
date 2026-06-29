package org.mabs.repository;

import org.mabs.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT a FROM Appointment a 
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
    SELECT a from Appointment a
    where a.patient.id = :patientId
    and a.status = 'completed'
    order by a.appointmentTime desc
""")
    List<Appointment> findCompletedByPatient(@Param("patientId") Long patientId);
}

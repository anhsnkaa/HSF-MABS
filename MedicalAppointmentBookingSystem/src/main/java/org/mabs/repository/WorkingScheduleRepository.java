package org.mabs.repository;

import org.mabs.entity.WorkingSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
    List<WorkingSchedule> findByDoctorIdAndWorkDate(Long doctorId, LocalDate date);

    List<WorkingSchedule> findByDoctorIdAndStatus(Long doctorId, String status);

    boolean existsByDoctorIdAndWorkDateAndStartTime(Long doctorId, LocalDate workDate, LocalTime startTime);

    boolean existsByDoctorIdAndWorkDateAndStartTimeAndIdNot(Long doctorId, LocalDate workDate, LocalTime startTime, Long id);

    @Query(
            value = """
                    SELECT ws FROM WorkingSchedule ws
                    JOIN FETCH ws.doctor d
                    JOIN FETCH d.user u
                    WHERE (:doctorId IS NULL OR d.id = :doctorId)
                      AND (:status IS NULL OR :status = '' OR ws.status = :status)
                      AND (:keyword IS NULL OR :keyword = ''
                           OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
                      AND (:fromDate IS NULL OR ws.workDate >= :fromDate)
                      AND (:toDate IS NULL OR ws.workDate <= :toDate)
                    ORDER BY ws.workDate DESC, ws.startTime ASC
                    """,
            countQuery = """
                    SELECT COUNT(ws) FROM WorkingSchedule ws
                    JOIN ws.doctor d
                    JOIN d.user u
                    WHERE (:doctorId IS NULL OR d.id = :doctorId)
                      AND (:status IS NULL OR :status = '' OR ws.status = :status)
                      AND (:keyword IS NULL OR :keyword = ''
                           OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
                      AND (:fromDate IS NULL OR ws.workDate >= :fromDate)
                      AND (:toDate IS NULL OR ws.workDate <= :toDate)
                    """
    )
    Page<WorkingSchedule> search(
            @Param("doctorId") Long doctorId,
            @Param("status") String status,
            @Param("keyword") String keyword,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);
}

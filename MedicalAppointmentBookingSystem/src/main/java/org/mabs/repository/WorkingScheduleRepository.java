package org.mabs.repository;

import org.mabs.entity.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
    List<WorkingSchedule> findByDoctorIdAndWorkDate(Long doctorId, LocalDate workDate);

    List<WorkingSchedule> findByDoctorIdAndStatus(Long doctorId, String status);
}

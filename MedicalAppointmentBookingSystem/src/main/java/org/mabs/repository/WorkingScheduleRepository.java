package org.mabs.repository;

import org.mabs.entity.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
    List<WorkingSchedule> findByDoctor_IdAndWorkDate(Long doctorId, LocalDate workDate);
}

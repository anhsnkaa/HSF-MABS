package org.mabs.service;

import org.mabs.dto.ScheduleBulkResult;
import org.mabs.dto.WorkingScheduleCreationDto;
import org.mabs.dto.WorkingScheduleMonthDto;
import org.mabs.dto.WorkingScheduleUpdateDto;
import org.mabs.entity.WorkingSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkingScheduleService {
    List<WorkingSchedule> getWorkingSchedules();

    Page<WorkingSchedule> searchSchedules(String keyword, Long doctorId, String status,
                                          Integer year, Integer month, Pageable pageable);

    WorkingSchedule createWorkingSchedule(WorkingScheduleCreationDto workingSchedule);
    ScheduleBulkResult createMonthlySchedule(WorkingScheduleMonthDto dto);
    WorkingSchedule updateWorkingSchedule(WorkingScheduleUpdateDto workingSchedule);
    void deleteWorkingSchedule(Long id);
    WorkingScheduleUpdateDto findWorkingScheduleById(Long id);
}

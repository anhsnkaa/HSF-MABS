package org.mabs.service;

import org.mabs.dto.TestResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TestResultService {
    TestResultDto uploadFile(MultipartFile file, Long appointmentId, Long patientId);
    List<TestResultDto> getTestResultsByPatient(Long patientId);
}

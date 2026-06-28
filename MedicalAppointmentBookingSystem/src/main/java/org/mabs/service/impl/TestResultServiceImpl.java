package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.dto.TestResultDto;
import org.mabs.entity.Appointment;
import org.mabs.entity.TestResult;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.TestResultRepository;
import org.mabs.service.TestResultService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestResultServiceImpl implements TestResultService {

    private final TestResultRepository testResultRepository;
    private final AppointmentRepository appointmentRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public TestResultDto uploadFile(MultipartFile file, Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String storedName = UUID.randomUUID().toString() + extension;

        String subDir = "test-results/" + patientId;
        Path targetDir = Paths.get(uploadDir, subDir);
        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload directory", e);
        }

        Path targetPath = targetDir.resolve(storedName);
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }

        String fileUrl = "/uploads/" + subDir + "/" + storedName;

        TestResult entity = new TestResult();
        entity.setAppointment(appointment);
        entity.setFileName(originalName);
        entity.setFileUrl(fileUrl);
        entity.setFileType(file.getContentType());
        entity.setUploadedAt(LocalDateTime.now());

        TestResult saved = testResultRepository.save(entity);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestResultDto> getTestResultsByPatient(Long patientId) {
        return testResultRepository.findByPatientId(patientId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private TestResultDto toDto(TestResult entity) {
        return new TestResultDto(
                entity.getId(),
                entity.getFileName(),
                entity.getFileUrl(),
                entity.getFileType(),
                entity.getUploadedAt(),
                entity.getAppointment().getId()
        );
    }
}

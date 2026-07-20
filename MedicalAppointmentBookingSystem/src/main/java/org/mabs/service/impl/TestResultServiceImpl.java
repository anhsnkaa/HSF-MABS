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
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestResultServiceImpl implements TestResultService {

    private final TestResultRepository testResultRepository;
    private final AppointmentRepository appointmentRepository;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png", "doc", "docx");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public TestResultDto uploadFile(MultipartFile file, Long appointmentId, Long patientId) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn file để tải lên");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new IllegalArgumentException("Lịch hẹn không thuộc về bạn");
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
        }
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Định dạng file không được hỗ trợ. Chỉ chấp nhận: " + String.join(", ", ALLOWED_EXTENSIONS));
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File vượt quá dung lượng cho phép (tối đa 10MB)");
        }

        String storedName = UUID.randomUUID().toString() + "." + extension;

        String subDir = "test-results/" + patientId;
        Path targetDir = Paths.get(uploadDir, subDir);
        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục lưu trữ", e);
        }

        Path targetPath = targetDir.resolve(storedName);
        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file", e);
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

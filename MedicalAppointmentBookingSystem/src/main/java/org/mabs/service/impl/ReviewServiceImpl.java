package org.mabs.service.impl;

import org.mabs.dto.ReviewDTO;
import org.mabs.entity.Appointment;
import org.mabs.entity.Doctor;
import org.mabs.entity.Review;
import org.mabs.entity.User;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.ReviewRepository;
import org.mabs.repository.UserRepository;
import org.mabs.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void submitReview(ReviewDTO dto, Long patientId) {
        if (dto == null || dto.getAppointmentId() == null) {
            throw new RuntimeException("Thông tin đánh giá không hợp lệ!");
        }

        // 1. Check if appointment exists
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));

        // 2. Check if user/patient exists
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng!"));

        // 3. Verify ownership
        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("Bạn không có quyền đánh giá lịch hẹn này!");
        }

        // 4. Verify appointment status is COMPLETED
        if (!"completed".equalsIgnoreCase(appointment.getStatus())) {
            throw new RuntimeException("Chỉ có thể đánh giá lịch hẹn đã hoàn thành!");
        }

        // 5. Verify review duplication
        if (reviewRepository.existsByAppointmentId(dto.getAppointmentId())) {
            throw new RuntimeException("Lịch hẹn này đã được đánh giá rồi!");
        }

        // 6. Validate rating range (1-5)
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new RuntimeException("Số sao đánh giá phải từ 1 đến 5 sao!");
        }

        // 7. Get Doctor directly from the appointment
        Doctor doctor = appointment.getDoctor();
        if (doctor == null) {
            throw new RuntimeException("Lịch hẹn chưa gắn với bác sĩ!");
        }

        // 8. Save the review
        Review review = new Review();
        review.setAppointment(appointment);
        review.setPatient(patient);
        review.setDoctor(doctor);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getReviewedAppointmentIds(Long patientId) {
        return reviewRepository.findReviewedAppointmentIdsByPatientId(patientId);
    }
}


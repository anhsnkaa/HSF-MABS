package org.mabs.service.impl;

import org.mabs.dto.ReviewDTO;
import org.mabs.entity.Appointment;
import org.mabs.entity.Doctor;
import org.mabs.entity.Review;
import org.mabs.entity.User;
import org.mabs.repository.AppointmentRepository;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.ReviewRepository;
import org.mabs.repository.UserRepository;
import org.mabs.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void submitReview(ReviewDTO dto, Long patientId) {
        // 1. Check if appointment exists
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn!"));

        // 2. Check if user/patient exists
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng!"));

        // 3. Check if doctor exists
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ!"));

        // 4. Verify ownership
        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("Bạn không có quyền đánh giá lịch hẹn này!");
        }

        // 5. Verify review duplication
        if (reviewRepository.existsByAppointmentId(dto.getAppointmentId())) {
            throw new RuntimeException("Lịch hẹn này đã được đánh giá rồi!");
        }

        // 6. Save the review
        Review review = new Review();
        review.setAppointment(appointment);
        review.setPatient(patient);
        review.setDoctor(doctor);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        reviewRepository.save(review);

        // 7. Calculate and update Doctor's ratingAvg and ratingCount
        Integer count = doctor.getRatingCount();
        BigDecimal avg = doctor.getRatingAvg();

        if (count == null || count == 0) {
            count = 0;
            avg = BigDecimal.ZERO;
        }
        if (avg == null) {
            avg = BigDecimal.ZERO;
        }

        int newCount = count + 1;
        double newTotal = (avg.doubleValue() * count) + dto.getRating();
        BigDecimal newAvg = BigDecimal.valueOf(newTotal / newCount).setScale(2, RoundingMode.HALF_UP);

        doctor.setRatingCount(newCount);
        doctor.setRatingAvg(newAvg);
        doctorRepository.save(doctor);
    }
}

package org.mabs.service;

import org.mabs.dto.ReviewDTO;
import java.util.List;

public interface ReviewService {
    void submitReview(ReviewDTO reviewDTO, Long patientId);
    List<Long> getReviewedAppointmentIds(Long patientId);
}


package org.mabs.service;

import org.mabs.dto.ReviewDTO;

public interface ReviewService {
    void submitReview(ReviewDTO reviewDTO, Long patientId);
}

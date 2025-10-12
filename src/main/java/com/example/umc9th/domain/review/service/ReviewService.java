package com.example.umc9th.domain.review.service;

import com.example.umc9th.domain.review.dto.ReviewRequest;
import com.example.umc9th.domain.review.dto.ReviewResponse;

public interface ReviewService {

    ReviewResponse.ReviewResultDTO createReview(Long memberId, Long storeId, ReviewRequest.ReviewCreateDTO request);
}

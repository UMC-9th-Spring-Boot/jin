package com.example.umc9th.domain.review.service;

import com.example.umc9th.domain.review.dto.ReviewRequest;
import com.example.umc9th.domain.review.dto.ReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewService {

    ReviewResponse.ReviewResultDTO createReview(Long memberId, Long storeId, ReviewRequest.ReviewCreateDTO request);

    ReviewResponse.ReviewListDTO getMyReviews(
            Long memberId,
            Long storeId,
            Integer rate,
            Long cursorId
    );
}

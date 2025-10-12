package com.example.umc9th.domain.review.converter;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.review.dto.ReviewRequest;
import com.example.umc9th.domain.review.dto.ReviewResponse;
import com.example.umc9th.domain.review.entity.Review;
import com.example.umc9th.domain.store.entity.Store;

public class ReviewConverter {

    // ReviewCreateDTO -> Review 엔터티
    public static Review toReview (ReviewRequest.ReviewCreateDTO dto, Member member, Store store) {
        return Review.builder()
                .content(dto.getContent())
                .rate(dto.getRate())
                .member(member)
                .store(store)
                .build();
    }

    // Review 엔터티 -> ReviewResultDTO
    public static ReviewResponse.ReviewResultDTO toReviewResultDTO (Review review) {
        return ReviewResponse.ReviewResultDTO.builder()
                .reviewId(review.getId())
                .build();
    }
}

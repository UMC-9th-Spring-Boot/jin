package com.example.umc9th.domain.review.converter;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.review.dto.ReviewRequest;
import com.example.umc9th.domain.review.dto.ReviewResponse;
import com.example.umc9th.domain.review.entity.Review;
import com.example.umc9th.domain.store.entity.Store;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 단일 리뷰 엔티티 → ReviewDetailDTO
    public static ReviewResponse.ReviewResultDTO toReviewResultDTO(Review review, List<String> images) {
        return ReviewResponse.ReviewResultDTO.builder()
                .reviewId(review.getId())
                .storeName(review.getStore().getName())
                .rate(review.getRate())
                .content(review.getContent())
                .reviewImages(images)
                .createdAt(review.getCreatedAt())
                .build();
    }

    // 리뷰 목록 변환
    public static List<ReviewResponse.ReviewResultDTO> toReviewResultDTOList(
            List<Review> reviews,
            Map<Long, List<String>> reviewImageMap
    ) {
        return reviews.stream()
                .map(review -> toReviewResultDTO(
                        review,
                        reviewImageMap.getOrDefault(review.getId(), List.of())
                ))
                .collect(Collectors.toList());
    }

}

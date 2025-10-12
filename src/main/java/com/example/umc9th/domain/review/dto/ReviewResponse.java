package com.example.umc9th.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReviewResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "리뷰 등록 응답 정보")
    public static class ReviewResultDTO {
        private Long reviewId;
    }
}

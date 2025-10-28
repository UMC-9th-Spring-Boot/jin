package com.example.umc9th.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "리뷰 응답 정보")
    public static class ReviewResultDTO {
        private Long reviewId;
        private String storeName;
        private Float rate;
        private String content;
        private List<String> reviewImages;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "리뷰 목록 조회 응답 정보")
    public static class ReviewListDTO {

        private List<ReviewResultDTO> reviewList;  // 리뷰 리스트
        private Boolean hasNext;                   // 다음 페이지 존재 여부
        private Long nextCursorId;                 // 다음 커서 ID (다음 요청에 사용)
    }

}

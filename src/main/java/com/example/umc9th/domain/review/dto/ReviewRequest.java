package com.example.umc9th.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class ReviewRequest {

    @Getter
    @Schema(description = "리뷰 등록 요청 정보")
    public static class ReviewCreateDTO {

        private Long storeId;
        private String content;
        private Float rate;
    }
}

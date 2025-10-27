package com.example.umc9th.domain.review.controller;

import com.example.umc9th.domain.review.dto.ReviewRequest;
import com.example.umc9th.domain.review.dto.ReviewResponse;
import com.example.umc9th.domain.review.service.ReviewService;
import com.example.umc9th.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 등록 API")
    public ApiResponse<ReviewResponse.ReviewResultDTO> createReview(
            @RequestBody ReviewRequest.ReviewCreateDTO request
    ){
        ReviewResponse.ReviewResultDTO response = reviewService.createReview(1L, request.getStoreId(), request);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/my")
    @Operation(summary = "나의 리뷰 조회 API", description = "가게별, 별점별(소수점 제외 몇점대)로 나의 리뷰를 조회할 수 있습니다.")
    public ApiResponse<ReviewResponse.ReviewListDTO> getMyReviews(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Integer rate,
            @RequestParam(required = false) Long cursorId){

        ReviewResponse.ReviewListDTO reviews =
                reviewService.getMyReviews(1L, storeId, rate, cursorId);

        return ApiResponse.onSuccess(reviews);

    }

}

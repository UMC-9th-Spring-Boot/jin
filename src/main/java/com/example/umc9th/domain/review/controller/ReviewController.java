package com.example.umc9th.domain.review.controller;

import com.example.umc9th.domain.review.dto.ReviewRequest;
import com.example.umc9th.domain.review.dto.ReviewResponse;
import com.example.umc9th.domain.review.service.ReviewService;
import com.example.umc9th.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<ReviewResponse.ReviewResultDTO> createReview(
            @RequestBody ReviewRequest.ReviewCreateDTO request
    ){
        ReviewResponse.ReviewResultDTO response = reviewService.createReview(1L, request.getStoreId(), request);
        return ApiResponse.onSuccess(response);
    }

}

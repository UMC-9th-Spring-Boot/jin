package com.example.umc9th.domain.review.repository;

import com.example.umc9th.domain.review.dto.ReviewResponse;
import com.example.umc9th.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

public interface ReviewRepositoryCustom {

    Slice<Review> findMyReviewsByFilter(
            Long memberId,
            Long storeId,
            Integer rate, // 정수로 (3.x -> 3점대)
            Long cursorId);

    // ReviewImage N + 1 해결용
    Map<Long, List<String>> findReviewImages(List<Long> reviewIds);


}

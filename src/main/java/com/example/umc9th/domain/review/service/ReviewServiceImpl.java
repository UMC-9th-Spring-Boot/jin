package com.example.umc9th.domain.review.service;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.member.repository.MemberRepository;
import com.example.umc9th.domain.review.converter.ReviewConverter;
import com.example.umc9th.domain.review.dto.ReviewRequest;
import com.example.umc9th.domain.review.dto.ReviewResponse;
import com.example.umc9th.domain.review.entity.Review;
import com.example.umc9th.domain.review.repository.ReviewRepository;
import com.example.umc9th.domain.review.repository.ReviewRepositoryCustom;
import com.example.umc9th.domain.store.entity.Store;
import com.example.umc9th.domain.store.repository.StoreRepository;
import com.example.umc9th.global.apiPayload.code.status.ErrorStatus;
import com.example.umc9th.global.apiPayload.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewRepositoryCustom reviewRepositoryCustom;

    private static final int PAGE_SIZE = 10;

    // 리뷰 작성
    @Transactional
    @Override
    public ReviewResponse.ReviewResultDTO createReview(Long memberId, Long storeId, ReviewRequest.ReviewCreateDTO request){

        // 1. 회원 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 2. 가게 엔티티 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.STORE_NOT_FOUND));

        // 3. DTO를 엔티티로 변환
        Review review = ReviewConverter.toReview(request, member, store);

        // 4. DB에 저장 (INSERT)
        reviewRepository.save(review);

        // 5. 응답 DTO로 변환하여 반환
        return ReviewConverter.toReviewResultDTO(review, List.of());
    }

    // 나의 리뷰 조회
    @Override
    public ReviewResponse.ReviewListDTO getMyReviews(
            Long memberId, Long storeId, Integer rate, Long cursorId) {

        // 1. Repository 호출 → DB 조회
        Slice<Review> reviewSlice = reviewRepositoryCustom.findMyReviewsByFilter(
                memberId, storeId, rate, cursorId);

        // 2. 리뷰 ID 리스트 추출
        List<Long> reviewIds = reviewSlice.getContent().stream()
                .map(Review::getId)
                .toList();

        // 3. 리뷰별 이미지 Map 조회
        Map<Long, List<String>> reviewImageMap = reviewRepositoryCustom.findReviewImages(reviewIds);

        // 4. Converter로 DTO 변환
        List<ReviewResponse.ReviewResultDTO> dtoList =
                ReviewConverter.toReviewResultDTOList(reviewSlice.getContent(), reviewImageMap);

        // 5. 마지막 커서 아이디 확인
        Long nextCursorId = reviewSlice.hasNext()
                ? dtoList.get(dtoList.size() - 1).getReviewId()
                : null;

        // 6. 응답 dto 반환
        return ReviewResponse.ReviewListDTO.builder()
                .reviewList(dtoList)
                .hasNext(reviewSlice.hasNext())
                .nextCursorId(nextCursorId)
                .build();
    }

    // 가게별 리뷰 조회
    public ReviewResponse.ReviewListDTO getReviewList(Long storeId, Long cursorId) {

        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Slice<Review> reviewSlice = reviewRepository.findReviewsByStore(storeId, cursorId, pageable);

        // 리뷰 ID 리스트 추출
        List<Long> reviewIds = reviewSlice.getContent().stream()
                .map(Review::getId)
                .toList();

        // 리뷰별 이미지 Map 조회 (N+1 문제 방지를 위해 별도의 Batch 조회)
        Map<Long, List<String>> reviewImageMap = reviewRepositoryCustom.findReviewImages(reviewIds);

        List<ReviewResponse.ReviewResultDTO> dtoList =
                ReviewConverter.toReviewResultDTOList(reviewSlice.getContent(), reviewImageMap);

        Long nextCursorId = null;
        if (reviewSlice.hasNext() && !dtoList.isEmpty()) {
            // 다음 페이지가 존재하고 DTO 리스트가 비어있지 않은 경우
            nextCursorId = dtoList.get(dtoList.size() - 1).getReviewId();
        }

        return ReviewResponse.ReviewListDTO.builder()
                .reviewList(dtoList)
                .hasNext(reviewSlice.hasNext())
                .nextCursorId(nextCursorId)
                .build();
    }

}

package com.example.umc9th.domain.review.repository;

import com.example.umc9th.domain.review.entity.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.umc9th.domain.review.entity.QReview.review;
import static com.example.umc9th.domain.review.entity.QReviewImage.reviewImage;
import static com.example.umc9th.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private static final int PAGE_SIZE = 10;

    @Override
    public Slice<Review> findMyReviewsByFilter(
            Long memberId,
            Long storeId,
            Integer rate,
            Long cursorId) {


        BooleanBuilder builder = new BooleanBuilder();

        // 1. 내가 쓴 리뷰
        builder.and(review.member.id.eq(memberId));

        // 2. 특정 가게 필터링
        if(storeId != null) {
            builder.and(review.store.id.eq(storeId));
        }

        // 3. 별점 필터링 (정수부분 기준 - 3점대, 4점대...)
        // goe : >= (이상), lt : < (미만)
        if(rate != null) {
            builder.and(review.rate.goe(rate).and(review.rate.lt(rate + 1)));
        }

        // 4. 커서 기반 무한스크롤
        if(cursorId != null) {
            builder.and(review.id.lt(cursorId));
        }

        // 5. query 실행
        List<Review> results = jpaQueryFactory
                .selectFrom(review)
                .leftJoin(review.store, store).fetchJoin() // store N+1 방지
                .where(builder)
                .orderBy(review.id.desc())
                .limit(PAGE_SIZE + 1) // 다음페이지 존재 여부 확인
                .fetch();

        // 6. slice 반환
        boolean hasNext = results.size() > PAGE_SIZE;
        if (hasNext) {
            results.remove(PAGE_SIZE); // +1 로 가져온건 삭제 - 다음 페이지 존재 여부 확인용 이니까
        }

        return new SliceImpl<>(results, PageRequest.of(0, PAGE_SIZE), hasNext);
    }

    // 리뷰 이미지 조회
    @Override
    public Map<Long, List<String>> findReviewImages(List<Long> reviewIds) {
        if(reviewIds == null || reviewIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // reviewId를 Key로, imageUrl 리스트를 value로 묶어서 반환
        return jpaQueryFactory
                .from(reviewImage)
                .where(reviewImage.review.id.in(reviewIds))
                .transform(GroupBy.groupBy(reviewImage.review.id)
                        .as(GroupBy.list(reviewImage.imgUrl)));
    }
}

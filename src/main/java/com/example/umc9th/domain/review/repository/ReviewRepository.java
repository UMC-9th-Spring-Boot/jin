package com.example.umc9th.domain.review.repository;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Review r WHERE r.member = :member")
    void deleteAllByMember(@Param("member") Member member);

    /**
     * 리뷰 작성하는 쿼리 - 메소드 생성 방식 사용
     * @param memberId 리뷰 작성자 ID
     * @param storeId 리뷰 대상 가게 ID
     * @param content 리뷰 내용
     * @param rate 별점
     */
    @Modifying // insert시에는 clearAutomatically가 불필요 - 영속성 컨텍스트와 데이터 불일치 발생 x
    @Query(value = "INSERT INTO review (member_id, store_id, content, rate, created_at, updated_at) " +
            "VALUES (:memberId, :storeId, :content, :rate, NOW(), NOW())",
            nativeQuery = true)
    void createReview(@Param("memberId") Long memberId,
                      @Param("storeId") Long storeId,
                      @Param("content") String content,
                      @Param("rate") Float rate);
}

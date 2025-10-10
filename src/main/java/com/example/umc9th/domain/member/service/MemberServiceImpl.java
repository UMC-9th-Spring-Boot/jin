package com.example.umc9th.domain.member.service;

import com.example.umc9th.domain.inquiry.repository.InquiryRepository;
import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.member.repository.*;
import com.example.umc9th.domain.mission.repository.MissionByMemberRepository;
import com.example.umc9th.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberAgreeRepository memberAgreeRepository;
    private final PointLogRepository pointLogRepository;
    private final NotificationRepository notificationRepository;
    private final MemberPreferCategoryRepository memberPreferCategoryRepository;
    private final MissionByMemberRepository missionByMemberRepository;
    private final ReviewRepository reviewRepository;
    private final InquiryRepository inquiryRepository;

    @Transactional
    @Override
    public void deleteMemberAndAll(Long memberId){

        // 1. 회원 조회 (없으면 예외 발생)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 회원을 찾을 수 없습니다: " + memberId));

        // 2. 연관된 자식 데이터들을 Batch Delete로 먼저 삭제
        memberAgreeRepository.deleteAllByMember(member);
        pointLogRepository.deleteAllByMember(member);
        notificationRepository.deleteAllByMember(member);
        memberPreferCategoryRepository.deleteAllByMember(member);
        missionByMemberRepository.deleteAllByMember(member);
        reviewRepository.deleteAllByMember(member);
        inquiryRepository.deleteAllByMember(member);

        // 3. 부모 엔티티인 Member 삭제
        memberRepository.delete(member);
    }
}

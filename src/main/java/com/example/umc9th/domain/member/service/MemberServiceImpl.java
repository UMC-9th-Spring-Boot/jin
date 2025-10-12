package com.example.umc9th.domain.member.service;

import com.example.umc9th.domain.inquiry.repository.InquiryRepository;
import com.example.umc9th.domain.member.dto.MemberResponse;
import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.member.repository.*;
import com.example.umc9th.domain.mission.repository.MissionByMemberRepository;
import com.example.umc9th.domain.review.repository.ReviewRepository;
import com.example.umc9th.global.apiPayload.code.status.ErrorStatus;
import com.example.umc9th.global.apiPayload.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    public MemberResponse.MemberDeleteResultDTO deleteMemberAndAll(Long memberId){

        // 1. 회원 조회 (없으면 예외 발생)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        log.info("회원 {}의 탈퇴 처리를 시작합니다.", member.getNickname());

        // 2. Member 삭제
        memberRepository.delete(member);
        log.info("회원 {}의 탈퇴 처리가 성공적으로 완료되었습니다.", member.getNickname());

        return new MemberResponse.MemberDeleteResultDTO(memberId, "회원 탈퇴가 성공적으로 처리되었습니다.");
    }
}

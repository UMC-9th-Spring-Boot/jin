package com.example.umc9th.domain.member.service;

import com.example.umc9th.domain.member.dto.MemberResponse;

public interface MemberService {

    // 회원 탈퇴 + 연관된 모든 데이터 삭제
    MemberResponse.MemberDeleteResultDTO deleteMemberAndAll(Long memberId);
}

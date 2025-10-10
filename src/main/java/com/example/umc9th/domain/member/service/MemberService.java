package com.example.umc9th.domain.member.service;

public interface MemberService {

    // 회원 탈퇴 + 연관된 모든 데이터 삭제
    void deleteMemberAndAll(Long memberId);
}

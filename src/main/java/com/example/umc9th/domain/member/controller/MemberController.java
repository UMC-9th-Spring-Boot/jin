package com.example.umc9th.domain.member.controller;

import com.example.umc9th.domain.member.dto.MemberResponse;
import com.example.umc9th.domain.member.repository.MemberRepository;
import com.example.umc9th.domain.member.service.MemberService;
import com.example.umc9th.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping("/{memberId}")
    public ApiResponse<MemberResponse.MemberDeleteResultDTO> deleteMember(@PathVariable Long memberId){
        return ApiResponse.onSuccess(memberService.deleteMemberAndAll(memberId));
    }
}

package com.example.umc9th.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 탈퇴 응답 정보")
    public static class MemberDeleteResultDTO{

        @Schema(description = "유저 상세 정보 ID", example = "1")
        private Long userId;

        private String message;
    }
}

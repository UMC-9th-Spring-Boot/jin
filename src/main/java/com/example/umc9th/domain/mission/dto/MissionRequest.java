package com.example.umc9th.domain.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MissionRequest {

    @Getter
    @Schema(description = "미션 등록 요청 정보")
    public static class MissionAddDTO {

        private String content;

        private LocalDate deadline;

        private Integer targetAmount;

        private Integer rewardPoint;
    }
}

package com.example.umc9th.domain.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MissionResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "나의 미션 목록의 개별 미션 응답")
    public static class MyMissionDTO {

        private String storeName;
        private String missionContent;
        private Integer rewardPoint;
        private Integer targetAmount;
        private LocalDateTime deadline;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "나의 미션 목록 조회 응답")
    public static class MyMissionListDTO {

        private List<MyMissionDTO> missionList;
        private Boolean hasNext;
        private LocalDateTime nextCursorDeadline;
        private Long nextCursorId;
    }
}

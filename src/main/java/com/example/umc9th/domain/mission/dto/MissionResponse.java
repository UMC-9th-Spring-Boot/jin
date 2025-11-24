package com.example.umc9th.domain.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class MissionResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "미션 목록의 개별 미션 응답")
    public static class MissionDTO {

        private String storeName;
        private String missionContent;
        private Integer rewardPoint;
        private Integer targetAmount;
        private LocalDate deadline;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "미션 목록 조회 응답")
    public static class MissionListDTO {

        private List<MissionDTO> missionList;
        private Boolean hasNext;
        private LocalDate nextCursorDeadline;
        private Long nextCursorId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "미션 추가 응답")
    public static class MissionAddResultDTO {
        private Long missionId;
    }
}

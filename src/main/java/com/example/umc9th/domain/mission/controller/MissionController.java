package com.example.umc9th.domain.mission.controller;

import com.example.umc9th.domain.mission.dto.MissionResponse;
import com.example.umc9th.domain.mission.entity.enums.MissionStatus;
import com.example.umc9th.domain.mission.service.MissionService;
import com.example.umc9th.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/my")
    @Operation(summary = "나의 미션 목록 조회 API", description = "진행중 또는 완료된 미션 목록을 커서 기반으로 조회합니다.")
    public ApiResponse<MissionResponse.MissionListDTO> getMyMissions(
            @RequestParam MissionStatus status,
            @RequestParam(required = false) LocalDate cursorDeadline,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") int size
    ) {
        MissionResponse.MissionListDTO response = missionService.getMyMissions(1L, status, cursorDeadline, cursorId, size);
        return ApiResponse.onSuccess(response);
    }

    @PatchMapping("/{mbmId}/complete")
    @Operation(summary = "미션 진행 완료 상태 변경 API", description = "진행 중인 미션의 상태를 완료(COMPLETE)로 변경하고 변경 결과를 반환합니다.")
    @Parameters({
            @Parameter(name = "mbmId", description = "변경할 MissionByMember의 ID")
    })
    public ApiResponse<MissionResponse.MissionDTO> completeMission(
            @PathVariable(name = "mbmId") Long mbmId
    ) {
        MissionResponse.MissionDTO response = missionService.completeMission(mbmId);
        return ApiResponse.onSuccess(response);
    }
}

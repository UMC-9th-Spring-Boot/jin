package com.example.umc9th.domain.mission.controller;

import com.example.umc9th.domain.mission.dto.MissionResponse;
import com.example.umc9th.domain.mission.entity.enums.MissionStatus;
import com.example.umc9th.domain.mission.service.MissionService;
import com.example.umc9th.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/my")
    @Operation(summary = "나의 미션 목록 조회 API", description = "진행중 또는 완료된 미션 목록을 커서 기반으로 조회합니다.")
    public ApiResponse<MissionResponse.MyMissionListDTO> getMyMissions(
            @RequestParam MissionStatus status,
            @RequestParam(required = false) LocalDateTime cursorDeadline,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") int size
    ) {
        MissionResponse.MyMissionListDTO response = missionService.getMyMissions(1L, status, cursorDeadline, cursorId, size);
        return ApiResponse.onSuccess(response);
    }

}

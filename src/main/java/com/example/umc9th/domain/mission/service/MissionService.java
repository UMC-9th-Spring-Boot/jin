package com.example.umc9th.domain.mission.service;

import com.example.umc9th.domain.mission.dto.MissionResponse;
import com.example.umc9th.domain.mission.entity.Mission;
import com.example.umc9th.domain.mission.entity.enums.MissionStatus;

import java.time.LocalDate;

public interface MissionService {

    // 나의 미션 목록 조회
    MissionResponse.MissionListDTO getMyMissions(Long memberId, MissionStatus status, LocalDate cursorDeadline, Long cursorId, int size);

    // 가게 미션 목록 조회
    MissionResponse.MissionListDTO getMissionListByStore(Long storeId, Long cursorId);

    // 미션 완료 처리
    MissionResponse.MissionDTO completeMission(Long mbmId);
}

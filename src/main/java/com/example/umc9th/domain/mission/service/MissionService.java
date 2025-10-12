package com.example.umc9th.domain.mission.service;

import com.example.umc9th.domain.mission.dto.MissionResponse;
import com.example.umc9th.domain.mission.entity.enums.MissionStatus;

import java.time.LocalDateTime;

public interface MissionService {

    // 나의 미션 목록 조회
    MissionResponse.MyMissionListDTO getMyMissions(Long memberId, MissionStatus status, LocalDateTime cursorDeadline, Long cursorId, int size);

}

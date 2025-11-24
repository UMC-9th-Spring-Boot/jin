package com.example.umc9th.domain.mission.converter;

import com.example.umc9th.domain.mission.dto.MissionRequest;
import com.example.umc9th.domain.mission.dto.MissionResponse;
import com.example.umc9th.domain.mission.entity.Mission;
import com.example.umc9th.domain.mission.entity.MissionByMember;
import com.example.umc9th.domain.store.entity.Store;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MissionConverter {

    public static MissionResponse.MissionDTO toMyMissionDTO(MissionByMember missionByMember) {
        Mission mission = missionByMember.getMission();
        return MissionResponse.MissionDTO.builder()
                .storeName(mission.getStore().getName())
                .missionContent(mission.getContent())
                .targetAmount(mission.getTargetAmount())
                .rewardPoint(mission.getRewardPoint())
                .deadline(mission.getDeadline())
                .build();
    }

    public static MissionResponse.MissionListDTO toMyMissionListDTO(Slice<MissionByMember> missionByMemberSlice) {

        // 각 MissionByMember 엔터티 -> MyMissionDTO로 변환 -> list로
        List<MissionResponse.MissionDTO> missionDTOList = missionByMemberSlice.getContent().stream()
                .map(MissionConverter::toMyMissionDTO)
                .collect(Collectors.toList());

        // 다음 페이지 조회를 위해 커서 값들을 초기화
        LocalDate nextCursorDeadline = null;
        Long nextCursorId = null;

        // 현재 페이지의 미션 목록이 비어있지 않다면, 다음 페이지를 조회할 커서 설정
        if (!missionDTOList.isEmpty()) {
            // 현재 페이지의 마지막 미션 엔티티
            Mission lastMission = missionByMemberSlice.getContent().get(missionByMemberSlice.getContent().size() - 1).getMission();

            // 마지막 미션의 마감 시간과 ID를 다음 커서 값으로 설정
            nextCursorDeadline = lastMission.getDeadline();
            nextCursorId = lastMission.getId();
        }

        return MissionResponse.MissionListDTO.builder()
                .missionList(missionDTOList)
                .hasNext(missionByMemberSlice.hasNext())
                .nextCursorDeadline(nextCursorDeadline)
                .nextCursorId(nextCursorId)
                .build();
    }

    // DTO -> Entity 변환
    public static Mission toMission(MissionRequest.MissionAddDTO request, Store store) {
        return Mission.builder()
                .store(store)
                .content(request.getContent())
                .deadline(request.getDeadline())
                .targetAmount(request.getTargetAmount())
                .rewardPoint(request.getRewardPoint())
                .build();
    }

    // Entity -> DTO 변환 (생성 응답)
    public static MissionResponse.MissionAddResultDTO toMissionAddResultDTO(Mission mission) {
        return MissionResponse.MissionAddResultDTO.builder()
                .missionId(mission.getId())
                .build();
    }

    // Mission Entity -> MissionDTO 변환
    public static MissionResponse.MissionDTO toMissionDTO(Mission mission) {
        return MissionResponse.MissionDTO.builder()
                .storeName(mission.getStore().getName())
                .missionContent(mission.getContent())
                .targetAmount(mission.getTargetAmount())
                .rewardPoint(mission.getRewardPoint())
                .deadline(mission.getDeadline())
                .build();
    }
}

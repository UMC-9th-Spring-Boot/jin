package com.example.umc9th.domain.mission.service;

import com.example.umc9th.domain.mission.converter.MissionConverter;
import com.example.umc9th.domain.mission.dto.MissionResponse;
import com.example.umc9th.domain.mission.entity.MissionByMember;
import com.example.umc9th.domain.mission.entity.enums.MissionStatus;
import com.example.umc9th.domain.mission.repository.MissionByMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionByMemberRepository missionByMemberRepository;

    @Override
    public MissionResponse.MyMissionListDTO getMyMissions(Long memberId, MissionStatus status, LocalDateTime cursorDeadline, Long cursorId, int size) {

        // 1. Pageable 객체 생성 (커서 기반이므로 페이지 번호는 항상 0)
        PageRequest pageRequest = PageRequest.of(0, size);

        // 2. Repository로 페이징된 데이터 조회
        Slice<MissionByMember> missionSlice = missionByMemberRepository.findMyMissionsWithCompoundCursor(
                memberId, status, cursorDeadline, cursorId, pageRequest
        );

        // 3. 응답 DTO로 변환
        return MissionConverter.toMyMissionListDTO(missionSlice);
    }

}

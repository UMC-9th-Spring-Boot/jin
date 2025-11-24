package com.example.umc9th.domain.mission.service;

import com.example.umc9th.domain.mission.converter.MissionConverter;
import com.example.umc9th.domain.mission.dto.MissionResponse;
import com.example.umc9th.domain.mission.entity.Mission;
import com.example.umc9th.domain.mission.entity.MissionByMember;
import com.example.umc9th.domain.mission.entity.enums.MissionStatus;
import com.example.umc9th.domain.mission.repository.MissionByMemberRepository;
import com.example.umc9th.domain.mission.repository.MissionRepository;
import com.example.umc9th.global.apiPayload.code.status.ErrorStatus;
import com.example.umc9th.global.apiPayload.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionByMemberRepository missionByMemberRepository;
    private static final int PAGE_SIZE = 10;
    private final MissionRepository missionRepository;

    @Override
    public MissionResponse.MissionListDTO getMyMissions(Long memberId, MissionStatus status, LocalDate cursorDeadline, Long cursorId, int size) {

        // 1. Pageable 객체 생성 (커서 기반이므로 페이지 번호는 항상 0)
        PageRequest pageRequest = PageRequest.of(0, size);

        // 2. Repository로 페이징된 데이터 조회
        Slice<MissionByMember> missionSlice = missionByMemberRepository.findMyMissionsWithCompoundCursor(
                memberId, status, cursorDeadline, cursorId, pageRequest
        );

        // 3. 응답 DTO로 변환
        return MissionConverter.toMyMissionListDTO(missionSlice);
    }

    @Override
    public MissionResponse.MissionListDTO getMissionListByStore(Long storeId, Long cursorId) {

        // 1. Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        // 2. Repository 호출
        Slice<Mission> missionSlice = missionRepository.findMissionsByStore(storeId, cursorId, pageable);

        // 3. DTO 변환 및 커서 계산
        List<MissionResponse.MissionDTO> missionDTOList = missionSlice.getContent().stream()
                .map(MissionConverter::toMissionDTO)
                .collect(Collectors.toList());

        Long nextCursorId = null;
        LocalDate nextCursorDeadline = null;

        if (missionSlice.hasNext() && !missionDTOList.isEmpty()) {
            // 다음 요청에 사용할 커서
            Mission lastMission = missionSlice.getContent().get(missionDTOList.size() - 1);
            nextCursorId = lastMission.getId();
            nextCursorDeadline = lastMission.getDeadline();
        }

        // 4. 응답 DTO 반환
        return MissionResponse.MissionListDTO.builder()
                .missionList(missionDTOList)
                .hasNext(missionSlice.hasNext())
                .nextCursorDeadline(nextCursorDeadline)
                .nextCursorId(nextCursorId)
                .build();
    }

    // 미션 진행 완료 처리
    @Transactional
    @Override
    public MissionResponse.MissionDTO completeMission(Long mbmId) {

        // 1. MissionByMember 엔티티 조회
        MissionByMember mbm = missionByMemberRepository.findById(mbmId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MISSION_BY_MEMBER_NOT_FOUND)); // 미션-멤버 관계 없음

        // 2. 현재 상태 확인 (IN_PROGRESS인지 확인)
        if (mbm.getStatus() != MissionStatus.IN_PROGRESS) {
            throw new ErrorHandler(ErrorStatus.MISSION_STATUS_NOT_IN_PROGRESS); // 진행 중인 미션이 아님
        }

        // 3. 상태 변경 (COMPLETE)
        // MissionByMember 엔티티에 updateStatus(MissionStatus status) 메서드가 존재한다고 가정
        mbm.updateStatus(MissionStatus.SUCCESS);

        // 4. 응답 DTO 변환 및 반환
        return MissionConverter.toMyMissionDTO(mbm);
    }
}

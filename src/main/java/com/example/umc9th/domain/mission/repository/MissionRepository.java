package com.example.umc9th.domain.mission.repository;

import com.example.umc9th.domain.mission.entity.Mission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    /**
     * 특정 지역에서 사용자가 아직 도전하지 않은 미션 목록을 커서 기반으로 조회
     * @param regionId   조회할 지역의 ID
     * @param memberId   현재 사용자의 ID (이미 시작한 미션을 필터링하기 위해)
     * @param cursorId   마지막으로 조회된 미션의 ID (첫 페이지는 null)
     * @param pageable   LIMIT 절과 페이징 처리를 위한 정보
     * @return Slice<Mission>
     */
    @Query("SELECT m FROM Mission m " +
            "JOIN m.store s " +
            "WHERE s.region.id = :regionId " +
            "  AND (:cursorId IS NULL OR m.id < :cursorId) " +
            "  AND NOT EXISTS (SELECT 1 FROM MissionByMember mbm WHERE mbm.member.id = :memberId AND mbm.mission = m) " +
            "ORDER BY m.id DESC")
    Slice<Mission> findAvailableMissionsByRegion(
            @Param("regionId") Long regionId,
            @Param("memberId") Long memberId,
            @Param("cursorId") Long cursorId,
            Pageable pageable);
}

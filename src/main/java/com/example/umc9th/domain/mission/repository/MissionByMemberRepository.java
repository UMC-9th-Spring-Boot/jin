package com.example.umc9th.domain.mission.repository;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.mission.entity.MissionByMember;
import com.example.umc9th.domain.mission.entity.enums.MissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MissionByMemberRepository extends JpaRepository<MissionByMember,Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MissionByMember mbm WHERE mbm.member = :member")
    void deleteAllByMember(@Param("member") Member member);

    /**
     * 내가 진행중, 진행완료한 미션 모아보는 쿼리
     * @param memberId       현재 사용자의 ID
     * @param status         조회할 미션 상태
     * @param cursorDeadline 마지막으로 조회된 미션의 마감일 (첫 페이지는 null)
     * @param cursorId       마지막으로 조회된 미션의 ID (첫 페이지는 null)
     * @param pageable       LIMIT 절과 페이징 처리를 위한 정보
     * @return Slice<MissionByMember>
     */
    @Query("SELECT mbm FROM MissionByMember mbm " +
            "JOIN mbm.mission m " +
            "WHERE mbm.member.id = :memberId " +
            "  AND mbm.status = :status " +
            "  AND m.deadline >= CURRENT_TIMESTAMP " +
            "  AND (:cursorDeadline IS NULL OR m.deadline > :cursorDeadline OR (m.deadline = :cursorDeadline AND m.id < :cursorId)) " +
            "ORDER BY m.deadline ASC, m.id DESC")
    Slice<MissionByMember> findMyMissionsWithCompoundCursor(
            @Param("memberId") Long memberId,
            @Param("status") MissionStatus status,
            @Param("cursorDeadline") LocalDateTime cursorDeadline,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );
}

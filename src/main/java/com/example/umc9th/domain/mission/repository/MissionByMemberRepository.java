package com.example.umc9th.domain.mission.repository;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.mission.entity.MissionByMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MissionByMemberRepository extends JpaRepository<MissionByMember,Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MissionByMember mbm WHERE mbm.member = :member")
    void deleteAllByMember(@Param("member") Member member);

}

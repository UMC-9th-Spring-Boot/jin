package com.example.umc9th.domain.member.repository;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.member.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM PointLog pl WHERE pl.member = :member")
    void deleteAllByMember(@Param("member") Member member);

}

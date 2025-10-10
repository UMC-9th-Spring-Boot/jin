package com.example.umc9th.domain.member.repository;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.member.entity.MemberAgree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberAgreeRepository extends JpaRepository<MemberAgree,Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MemberAgree ma WHERE ma.member = :member")
    void deleteAllByMember(@Param("member") Member member);

}

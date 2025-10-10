package com.example.umc9th.domain.member.repository;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.member.entity.MemberPreferCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberPreferCategoryRepository extends JpaRepository<MemberPreferCategory,Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MemberPreferCategory mpc WHERE mpc.member = :member")
    void deleteAllByMember(@Param("member") Member member);

}

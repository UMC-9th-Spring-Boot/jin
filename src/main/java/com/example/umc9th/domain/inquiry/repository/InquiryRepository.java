package com.example.umc9th.domain.inquiry.repository;

import com.example.umc9th.domain.inquiry.entity.Inquiry;
import com.example.umc9th.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Inquiry i WHERE i.member = :member")
    void deleteAllByMember(@Param("member") Member member);

}

package com.example.umc9th.domain.member.repository;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.member.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Notification n WHERE n.member = :member")
    void deleteAllByMember(@Param("member") Member member);

}

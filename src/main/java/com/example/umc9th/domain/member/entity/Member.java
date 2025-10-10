package com.example.umc9th.domain.member.entity;

import com.example.umc9th.domain.inquiry.entity.Inquiry;
import com.example.umc9th.domain.member.entity.enums.Gender;
import com.example.umc9th.domain.member.entity.enums.MemberStatus;
import com.example.umc9th.domain.member.entity.enums.SocialLoginType;
import com.example.umc9th.domain.mission.entity.MissionByMember;
import com.example.umc9th.domain.review.entity.Review;
import com.example.umc9th.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    @Column(length = 15)
    private String phoneNumber;

    private Boolean isPhoneVerified;

    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    private LocalDate inactiveDate;

    // 연관관계 매핑
    // 1. 약관 동의 (MemberAgree)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberAgree> memberAgreeList = new ArrayList<>();

    // 2. 포인트 내역 (PointLog)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PointLog> pointLogList = new ArrayList<>();

    // 3. 알림 (Notification)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notification> notificationList = new ArrayList<>();

    // 4. 선호 음식 카테고리 (MemberPreferCategory)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberPreferCategory> memberPreferCategoryList = new ArrayList<>();

    // 5. 멤버별 미션 (MissionByMember)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MissionByMember> missionByMemberList = new ArrayList<>();

    // 6. 리뷰 (Review)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    // 7. 문의 (Inquiry)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Inquiry> inquiryList = new ArrayList<>();
}

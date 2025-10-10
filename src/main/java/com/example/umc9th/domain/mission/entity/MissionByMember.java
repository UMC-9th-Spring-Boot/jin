package com.example.umc9th.domain.mission.entity;

import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.domain.mission.entity.enums.MissionStatus;
import com.example.umc9th.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MissionByMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'IN_PROGRESS'")
    private MissionStatus status = MissionStatus.IN_PROGRESS; // 기본값: 진행중

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isReviewed = false;

}

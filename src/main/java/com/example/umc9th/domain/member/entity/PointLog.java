package com.example.umc9th.domain.member.entity;

import com.example.umc9th.domain.member.entity.enums.PointType;
import com.example.umc9th.global.apiPayload.code.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType pointType;

    @Column(nullable = true)
    private Integer point;

    @Column(nullable = false, length = 20)
    private String title; // 포인트 변경 제목

    @Column(length = 50)
    private String description; // 상세 설명

    @Column(nullable = false)
    private Integer balance; // 해당 내역 반영 후 최종 잔액

}

package com.example.umc9th.domain.inquiry.entity;

import com.example.umc9th.domain.inquiry.entity.enums.InquiryType;
import com.example.umc9th.domain.member.entity.Member;
import com.example.umc9th.global.apiPayload.code.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Inquiry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 30)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryType type;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isAnswered = false;

    // 연관관계 매핑
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InquiryImage> inquiryImgList = new ArrayList<>();
}

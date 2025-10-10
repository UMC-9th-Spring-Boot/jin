package com.example.umc9th.domain.member.entity;

import com.example.umc9th.domain.store.entity.Store;
import com.example.umc9th.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FoodCategory extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String name;

    // 연관관계 매핑
    @OneToMany(mappedBy = "foodCategory", cascade = CascadeType.ALL)
    private List<Store> storeList = new ArrayList<>();

}

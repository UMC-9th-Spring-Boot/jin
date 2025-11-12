package com.example.umc9th.domain.store.service;

import com.example.umc9th.domain.mission.dto.MissionRequest;
import com.example.umc9th.domain.mission.entity.Mission;
import com.example.umc9th.domain.store.dto.StoreResponse;
import com.example.umc9th.domain.store.entity.enums.StoreSortType;

public interface StoreService {

    StoreResponse.StoreListDTO searchStores(
            String region,
            String keyword,
            StoreSortType sortType,
            Long cursorId
    );

    /**
     * 가게에 미션을 추가하는 로직
     * @param storeId 가게 ID
     * @param request 미션 추가 DTO
     * @return 생성된 Mission 엔티티
     */
    Mission addMissionToStore(Long storeId, MissionRequest.MissionAddDTO request);
}

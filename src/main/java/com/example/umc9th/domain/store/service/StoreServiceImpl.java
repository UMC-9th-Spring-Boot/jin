package com.example.umc9th.domain.store.service;

import com.example.umc9th.domain.mission.converter.MissionConverter;
import com.example.umc9th.domain.mission.dto.MissionRequest;
import com.example.umc9th.domain.mission.entity.Mission;
import com.example.umc9th.domain.mission.repository.MissionRepository;
import com.example.umc9th.domain.store.converter.StoreConverter;
import com.example.umc9th.domain.store.dto.StoreResponse;
import com.example.umc9th.domain.store.entity.Store;
import com.example.umc9th.domain.store.entity.enums.StoreSortType;
import com.example.umc9th.domain.store.repository.StoreRepository;
import com.example.umc9th.domain.store.repository.StoreRepositoryCustom;
import com.example.umc9th.global.apiPayload.code.status.ErrorStatus;
import com.example.umc9th.global.apiPayload.exception.handler.ErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepositoryCustom storeRepositoryCustom;
    private final StoreRepository storeRepository;
    private final MissionRepository missionRepository;

    @Override
    public StoreResponse.StoreListDTO searchStores(
            String region, String keyword, StoreSortType sortType, Long cursorId
    ) {
        Slice<Store> storeSlice = storeRepositoryCustom.searchStores(region, keyword, sortType, cursorId);
        return StoreConverter.toStoreListDTO(storeSlice);
    }

    @Transactional
    @Override
    public Mission addMissionToStore(Long storeId, MissionRequest.MissionAddDTO request) {

        // 1. 가게(Store) 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.STORE_NOT_FOUND));

        // 2. DTO -> Entity 변환
        Mission newMission = MissionConverter.toMission(request, store);

        // 3. Mission 저장
        return missionRepository.save(newMission);
    }
}

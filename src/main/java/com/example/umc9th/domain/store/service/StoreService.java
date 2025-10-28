package com.example.umc9th.domain.store.service;

import com.example.umc9th.domain.store.dto.StoreResponse;
import com.example.umc9th.domain.store.entity.enums.StoreSortType;

public interface StoreService {

    StoreResponse.StoreListDTO searchStores(
            String region,
            String keyword,
            StoreSortType sortType,
            Long cursorId
    );
}

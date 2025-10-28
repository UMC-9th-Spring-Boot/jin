package com.example.umc9th.domain.store.repository;

import com.example.umc9th.domain.store.entity.Store;
import com.example.umc9th.domain.store.entity.enums.StoreSortType;
import org.springframework.data.domain.Slice;

public interface StoreRepositoryCustom {

    // 가게 검색
    Slice<Store> searchStores(String region, String keyword, StoreSortType sortType, Long cursorId);

}

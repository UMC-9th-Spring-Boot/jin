package com.example.umc9th.domain.store.service;

import com.example.umc9th.domain.store.converter.StoreConverter;
import com.example.umc9th.domain.store.dto.StoreResponse;
import com.example.umc9th.domain.store.entity.Store;
import com.example.umc9th.domain.store.entity.enums.StoreSortType;
import com.example.umc9th.domain.store.repository.StoreRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepositoryCustom storeRepositoryCustom;

    @Override
    public StoreResponse.StoreListDTO searchStores(
            String region, String keyword, StoreSortType sortType, Long cursorId
    ) {
        Slice<Store> storeSlice = storeRepositoryCustom.searchStores(region, keyword, sortType, cursorId);
        return StoreConverter.toStoreListDTO(storeSlice);
    }
}

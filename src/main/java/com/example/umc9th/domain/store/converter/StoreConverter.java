package com.example.umc9th.domain.store.converter;

import com.example.umc9th.domain.store.dto.StoreResponse;
import com.example.umc9th.domain.store.entity.Store;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class StoreConverter {

    public static StoreResponse.StoreResultDTO toStoreResultDTO(Store store) {
        return StoreResponse.StoreResultDTO.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .region(store.getRegion().getName())
                .address(store.getAddress())
                .rateAvg(store.getRateAvg())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .build();
    }

    public static StoreResponse.StoreListDTO toStoreListDTO(Slice<Store> storeSlice) {
        List<StoreResponse.StoreResultDTO> dtoList = storeSlice.getContent().stream()
                .map(StoreConverter::toStoreResultDTO)
                .collect(Collectors.toList());

        Long nextCursorId = storeSlice.hasNext()
                ? dtoList.get(dtoList.size() - 1).getStoreId()
                : null;

        return StoreResponse.StoreListDTO.builder()
                .storeList(dtoList)
                .hasNext(storeSlice.hasNext())
                .nextCursorId(nextCursorId)
                .build();
    }
}

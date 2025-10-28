package com.example.umc9th.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class StoreResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "가게 검색 개별 결과 DTO")
    public static class StoreResultDTO {
        private Long storeId;
        private String storeName;
        private String region;
        private String address;
        private Float rateAvg;
        private LocalDateTime openTime;
        private LocalDateTime closeTime;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "커서 기반 가게 검색 결과 DTO")
    public static class StoreListDTO {
        private List<StoreResultDTO> storeList;
        private Boolean hasNext;
        private Long nextCursorId; // 다음 요청 시 사용할 커서 ID
    }
}

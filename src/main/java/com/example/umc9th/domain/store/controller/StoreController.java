package com.example.umc9th.domain.store.controller;

import com.example.umc9th.domain.mission.converter.MissionConverter;
import com.example.umc9th.domain.mission.dto.MissionRequest;
import com.example.umc9th.domain.mission.dto.MissionResponse;
import com.example.umc9th.domain.mission.entity.Mission;
import com.example.umc9th.domain.store.dto.StoreResponse;
import com.example.umc9th.domain.store.entity.enums.StoreSortType;
import com.example.umc9th.domain.store.service.StoreService;
import com.example.umc9th.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @Operation(
            summary = "커서 기반 가게 검색",
            description = """
            지역(region), 이름(keyword), 정렬(sort) 조건으로 가게를 검색합니다.
            - region: 지역 필터 (예: 강남구)
            - keyword: 검색어 (공백 포함 시 단어별 OR 검색)
            - sort: latest(최신순), name(이름순)
            - cursorId: 이전 요청의 마지막 storeId
            """
    )
    @GetMapping("/search")
    public ApiResponse<StoreResponse.StoreListDTO> searchStores(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "LATEST") StoreSortType sortType,
            @RequestParam(required = false) Long cursorId
    ) {
        StoreResponse.StoreListDTO response = storeService.searchStores(region, keyword, sortType, cursorId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "가게에 미션 추가 API", description = "특정 가게(store)에 새로운 미션을 추가합니다.")
    @PostMapping("/{storeId}/missions")
    public ApiResponse<MissionResponse.MissionAddResultDTO> addMission(
            @PathVariable(name = "storeId") Long storeId,
            @RequestBody @Valid MissionRequest.MissionAddDTO request
    ) {
        // 3. StoreCommandService의 메소드 호출
        Mission newMission = storeService.addMissionToStore(storeId, request);

        // 4. MissionConverter로 응답 DTO 변환
        MissionResponse.MissionAddResultDTO response = MissionConverter.toMissionAddResultDTO(newMission);

        return ApiResponse.onSuccess(response);
    }
}

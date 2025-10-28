package com.example.umc9th.domain.store.repository;

import com.example.umc9th.domain.region.entity.QRegion;
import com.example.umc9th.domain.store.entity.Store;
import com.example.umc9th.domain.store.entity.enums.StoreSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.umc9th.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final int PAGE_SIZE = 10;

    @Override
    public Slice<Store> searchStores(
            String region, String keyword, StoreSortType sortType, Long cursorId
    ) {

        QRegion qRegion = QRegion.region;

        // 1. Predicate 조립
        BooleanExpression predicate = createPredicate(region, keyword);

        // 2. 커서 조건 추가 (cursorId보다 작은 id만)
        if (cursorId != null) {
            predicate = Expressions.allOf(predicate, store.id.lt(cursorId));
        }

        // 3. 정렬 조건
        OrderSpecifier<?> orderSpecifier =
                (sortType == StoreSortType.NAME)
                        ? store.name.asc()
                        : store.createdAt.desc();

        // 4. 쿼리 실행 (limit + 1로 다음 페이지 존재 여부 확인)
        List<Store> results = queryFactory
                .selectFrom(store)
                .leftJoin(store.region, qRegion).fetchJoin()
                .where(predicate)
                .orderBy(orderSpecifier, store.createdAt.desc())
                .limit(PAGE_SIZE + 1)
                .fetch();

        // 5. hasNext 계산
        boolean hasNext = results.size() > PAGE_SIZE;
        if (hasNext) {
            results.remove(PAGE_SIZE); // +1 개 데이터 제거
        }

        return new SliceImpl<>(results, PageRequest.of(0, PAGE_SIZE), hasNext);

    }

    // Predicate 생성 메서드
    private BooleanExpression createPredicate(String region, String keyword) {

        // 기본값 : true (처음에는 아무 조건이 없는 상태)
        BooleanExpression condition = Expressions.TRUE.isTrue();

        // 지역 필터
        if (region != null && !region.isBlank()) {
            condition = condition.and(store.region.name.eq(region));
        }

        // 이름 검색
        if (keyword != null && !keyword.isBlank()) {
            if (keyword.contains(" ")) {
                // 공백 포함 → 단어별 OR 검색
                String[] words = keyword.split("\\s+"); // 공백을 기준으로 문자열 분리
                BooleanExpression keywordCondition = null; // 단어별 조건을 조립하기 위한 임시 변수 - 초기: null로 시작
                for (String word : words) {
                    BooleanExpression expr = store.name.containsIgnoreCase(word); //조건을 하나씩 붙힘
                    keywordCondition = (keywordCondition == null)
                            ? expr // 첫번째 단어 - 초기화
                            : keywordCondition.or(expr); // 두번째 부터는 or 조건으로 붙힘
                }
                condition = condition.and(keywordCondition); // 최종적으로 조립된을 전체 조건에 and로 연결
            } else {
                // 공백 없음 → 전체 단어 포함
                condition = condition.and(store.name.containsIgnoreCase(keyword));
            }
        }

        return condition;
    }
}

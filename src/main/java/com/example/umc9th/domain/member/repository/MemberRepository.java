package com.example.umc9th.domain.member.repository;

import com.example.umc9th.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 회원 ID를 기준으로 회원 정보를 조회
     * JPA 기본 메서드인 findById(id)와 완전히 동일하게 동작 (사실상 불필요한 코드)
     * @param memberId 조회할 회원의 ID
     * @return Optional<Member>
     */
    Optional<Member> findMemberById(Long memberId);
}

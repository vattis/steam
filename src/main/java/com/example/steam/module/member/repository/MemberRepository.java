package com.example.steam.module.member.repository;

import com.example.steam.module.member.domain.Member;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Cacheable(value="memberSpringCache", key = "'member:' + #email", unless = "#result == null")
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}

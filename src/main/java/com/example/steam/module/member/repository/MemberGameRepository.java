package com.example.steam.module.member.repository;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.domain.MemberGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberGameRepository extends JpaRepository<MemberGame, Long> {
    List<MemberGame> findAllByMember(Member member);
}

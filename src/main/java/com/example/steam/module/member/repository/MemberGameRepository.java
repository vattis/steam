package com.example.steam.module.member.repository;

import com.example.steam.module.member.domain.MemberGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGameRepository extends JpaRepository<MemberGame, Long> {
}

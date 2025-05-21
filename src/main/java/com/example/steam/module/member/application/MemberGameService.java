package com.example.steam.module.member.application;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.domain.MemberGame;
import com.example.steam.module.member.repository.MemberGameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberGameService {
    private final MemberGameRepository memberGameRepository;

    List<MemberGame> getMemberGamesByMemberId(Member member){
        return memberGameRepository.findAllByMember(member);
    }
}

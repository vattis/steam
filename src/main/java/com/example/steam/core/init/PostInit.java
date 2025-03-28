package com.example.steam.core.init;

import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostInit {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    @PostConstruct
    public void init(){
        for(int i = 1; i <= 10; i++){
            memberRepository.save(Member.makeSample(i));
        }

    }

}

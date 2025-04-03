package com.example.steam.module.member.application;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.dto.SignUpForm;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //회원가입 서비스
    public Member addMember(SignUpForm signUpForm){
        if(!isValid(signUpForm)){
            throw(new RuntimeException());
        }
        Member member = Member.of(signUpForm.getNickname(), signUpForm.getEmail(), signUpForm.getPassword());
        return memberRepository.save(member);
    }

    //회원 정보 수정
    public Member updateMember(SignUpForm signUpForm){
        if(!signUpForm.isValid()){
            throw new RuntimeException();
        }
        Member member = memberRepository.findByEmail(signUpForm.getEmail()).orElseThrow(NoSuchElementException::new);
        return member.update(signUpForm.getNickname(), signUpForm.getPassword());
    }

    //회원 정보 검색
    public Member findMember(Long id){
        return memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    //회원가입 유효성 검증
    public boolean isValid(SignUpForm signUpForm){
        return signUpForm.isValid() && !memberRepository.existsByEmail(signUpForm.getEmail());
    }

    //회원 가입 이메일 인증 코드 만들기
    public int createAuthCode(){
        Random random = new Random();
        return random.nextInt(900000)+100000;
    }

}

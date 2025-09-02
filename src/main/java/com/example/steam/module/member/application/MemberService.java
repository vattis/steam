package com.example.steam.module.member.application;

import com.example.steam.module.comment.dto.ProfileCommentDto;
import com.example.steam.module.comment.repository.ProfileCommentRepository;
import com.example.steam.module.friendship.dto.SimpleFriendshipDto;
import com.example.steam.module.friendship.repository.FriendshipRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.dto.ProfileDto;
import com.example.steam.module.member.dto.SignUpForm;
import com.example.steam.module.member.dto.MemberGameDto;
import com.example.steam.module.member.repository.MemberGameRepository;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberGameService memberGameService;
    private final ProfileCommentRepository profileCommentRepository;
    private final FriendshipRepository friendshipRepository;
    //회원가입 서비스
    public Member addMember(SignUpForm signUpForm){
        if(!isValid(signUpForm)){
            throw(new RuntimeException());
        }
        Member member = Member.of(signUpForm.getNickname(), signUpForm.getEmail(), signUpForm.getPassword());
        return memberRepository.save(member);
    }

    //회원 정보 수정
    @CachePut(value = "SpringCache", key = "#signUpForm.email")
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

    //이메일을 통한 회원 검색
    @Cacheable(value="SpringCache", key = "'member:' + #email", unless = "#result == null")
    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(NoSuchElementException::new); }

    //회원가입 유효성 검증
    public boolean isValid(SignUpForm signUpForm){
        return signUpForm.isValid() && !memberRepository.existsByEmail(signUpForm.getEmail());
    }

    //회원 가입 이메일 인증 코드 만들기
    public int createAuthCode(){
        Random random = new Random();
        return random.nextInt(900000)+100000;
    }

    //프로필 정보 검색 및 dto 생성
    public ProfileDto getProfile(Long profileMemberId, PageRequest pageRequest){
        Member profileMember = memberRepository.findById(profileMemberId).orElseThrow(NoSuchElementException::new);
        List<MemberGameDto> memberGames = memberGameService.findTop5DtoByMember(profileMember);
        Page<ProfileCommentDto> profileCommentPage = profileCommentRepository.findDtoByProfileMember(profileMember, pageRequest);
        List<SimpleFriendshipDto> friendships = friendshipRepository.findAllByFromMemberId(profileMemberId).stream().map(SimpleFriendshipDto::from).toList();
        return ProfileDto.of(profileMember, memberGames, profileCommentPage, friendships);
    }
}

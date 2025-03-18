package com.example.steam.module.member.application;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.dto.SignUpForm;
import com.example.steam.module.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock MemberRepository memberRepository;
    @InjectMocks MemberService memberService;

    @Test
    void addMember() {
        //given
        SignUpForm correctSignUpForm = SignUpForm.of("email", "password", "password", "nickname");
        SignUpForm signUpFormWithNull = SignUpForm.of(null, "password", "password", "nickname");
        SignUpForm signUpFormWithPasswordErr = SignUpForm.of("email", "password", "passwordDiff", "nickname");

        Member correctMember = Member.of(correctSignUpForm.getNickname(), correctSignUpForm.getEmail(), correctSignUpForm.getPassword());
        ReflectionTestUtils.setField(correctMember, "id", 1L);

        given(memberRepository.save(any(Member.class))).willReturn(correctMember);
        given(memberRepository.existsByEmail(any(String.class))).willReturn(false);

        //when
        Member member = memberService.addMember(correctSignUpForm);
        RuntimeException exception1 = assertThrows(RuntimeException.class, ()->memberService.addMember(signUpFormWithNull));
        RuntimeException exception2 = assertThrows(RuntimeException.class, ()->memberService.addMember(signUpFormWithPasswordErr));

        //then
        assertThat(member).isEqualTo(correctMember);
        assertThat(exception1).isInstanceOf(RuntimeException.class);
        assertThat(exception2).isInstanceOf(RuntimeException.class);

    }

    @Test
    void updateMember() {
        //given
        SignUpForm newSignUpForm = SignUpForm.of("email", "newPassword", "newPassword", "newNickname");
        SignUpForm signUpFormWithNull = SignUpForm.of(null, "password", "password", "nickname");
        SignUpForm signUpFormWithPasswordErr = SignUpForm.of("email", "password", "passwordDiff", "nickname");
        Member oldMember = Member.of("nickname", "email", "password");

        given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(oldMember));

        //when
        Member memberResult = memberService.updateMember(newSignUpForm);
        RuntimeException exception1 = assertThrows(RuntimeException.class, ()->memberService.updateMember(signUpFormWithNull));
        RuntimeException exception2 = assertThrows(RuntimeException.class, ()->memberService.updateMember(signUpFormWithPasswordErr));

        //then
        assertThat(memberResult.getNickname()).isEqualTo(newSignUpForm.getNickname());
        assertThat(memberResult.getPassword()).isEqualTo(newSignUpForm.getPassword());
        assertThat(exception1).isInstanceOf(RuntimeException.class);
        assertThat(exception2).isInstanceOf(RuntimeException.class);
    }

    @Test
    void isValid() {
        //given
        SignUpForm newSignUpForm = SignUpForm.of("email", "newPassword", "newPassword", "newNickname");
        SignUpForm signUpFormWithNull = SignUpForm.of(null, "password", "password", "nickname");
        SignUpForm signUpFormWithPasswordErr = SignUpForm.of("email", "password", "passwordDiff", "nickname");
        given(memberRepository.existsByEmail(any(String.class))).willReturn(false);

        //when
        Boolean result1 = memberService.isValid(newSignUpForm);
        Boolean result2 = memberService.isValid(signUpFormWithNull);
        Boolean result3 = memberService.isValid(signUpFormWithPasswordErr);
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
    }
}
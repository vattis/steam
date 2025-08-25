package com.example.steam.module.member.application;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.domain.MemberUserDetails;
import com.example.steam.module.member.dto.CurrentMemberDto;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저 [" + username + "]을 찾지 못했습니다."));
        return createUserDetails(member);
    }

    private UserDetails createUserDetails(Member member){
        List<String> role = new ArrayList<>();
        role.add(member.getRole().getLabel());
        return MemberUserDetails.builder()
                .id(member.getId())
                .username(member.getEmail())
                .password(passwordEncoder.encode(member.getPassword()))
                .currentMemberDto(CurrentMemberDto.from(member))
                .roles(role)
                .build();
    }
}

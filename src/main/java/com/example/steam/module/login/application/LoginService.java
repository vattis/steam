package com.example.steam.module.login.application;

import com.example.steam.core.security.jwt.JwtProvider;
import com.example.steam.core.security.jwt.JwtToken;
import com.example.steam.module.login.dto.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public JwtToken login(LoginForm loginForm) {
        //검증되지 않은 AuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword());

        Authentication authentication = makeAuthentication(authenticationToken);
        return jwtProvider.generateToken(authentication);

        //JwtToken 생성
    }

    private void logout(){

    }

    private Authentication makeAuthentication(UsernamePasswordAuthenticationToken authenticationToken) {
        /*
            AuthenticationManager가 authenticate() 메서드를 실행하면 검증 진행
            UserDetailsService를 구현한 CustomUserDetailsService의 loadUserByUsername()메서드를 찾아서 내부에서 실행
            DB로부터 해당되는 Member를 찾아와서 검증된 Authentication으로 반환
        */
        try {
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);
            log.info("로그인 성공: {}", authentication.getName());
            return authentication;
        } catch (UsernameNotFoundException e) {
            log.warn("사용자 정보를 찾을 수 없습니다: {}", e.getMessage());
            throw e; // 또는 사용자에게 친절한 예외 변환
        } catch (BadCredentialsException e) {
            log.warn("비밀번호가 일치하지 않습니다: {}", e.getMessage());
            throw e;
        } catch (AuthenticationException e) {
            log.warn("인증 실패: {}", e.getMessage());
            throw e;
        }
    }
}

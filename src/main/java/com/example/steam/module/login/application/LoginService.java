package com.example.steam.module.login.application;

import com.example.steam.core.jwt.JwtProvider;
import com.example.steam.core.jwt.JwtToken;
import com.example.steam.module.login.dto.LoginForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public JwtToken login(LoginForm loginForm) {
        //검증되지 않은 AuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword());


        /*
    AuthenticationManager가 authenticate() 메서드를 실행하면 검증 진행
    UserDetailsService를 구현한 CustomUserDetailsService의 loadUserByUsername()메서드를 찾아서 내부에서 실행
    DB로부터 해당되는 Member를 찾아와서 검증된 Authentication으로 반환
    */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


        //JwtToken 생성
        return jwtProvider.generateToken(authentication);
    }
}

package com.example.steam.module.login.presentation;

import com.example.steam.core.security.jwt.JwtConst;
import com.example.steam.core.security.jwt.JwtToken;
import com.example.steam.module.login.application.LoginService;
import com.example.steam.module.login.dto.LoginForm;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.dto.CurrentUserDto;
import com.example.steam.module.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {
    private final MemberRepository memberRepository;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginForm loginForm,
            HttpServletResponse response) {

        log.info("[AuthApiController]-login email: {}", loginForm.getEmail());
        JwtToken token = loginService.login(loginForm);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", token.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtConst.JWT_ACCESS_TOKEN_EXPIRES_IN)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtConst.JWT_REFRESH_TOKEN_EXPIRES_IN)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(Map.of("message", "로그인 성공"));
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserDto> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(null);
        }

        Member member = memberRepository.findByEmail(principal.getName()).orElse(null);
        if (member == null) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(CurrentUserDto.from(member));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        loginService.logout(request, response);
        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }
}

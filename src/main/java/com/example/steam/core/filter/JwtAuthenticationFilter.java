package com.example.steam.core.filter;

import com.example.steam.core.security.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;

    //request에서 token을 추출 -> 유효성 검사 -> authentication 변환 -> SecurityContext에 authentication 저장
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (isExcluded(request.getRequestURI())) { //예외처리 해야하는 uri 설정
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try{
            String token = jwtProvider.resolveAccessToken((HttpServletRequest) servletRequest);
            if(StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (ExpiredJwtException e){
            log.info("토큰 만료: /login 으로 리다렉트");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendRedirect("/login");
        }
    }

    //request에서 token 추출
    private boolean isExcluded(String uri) {
        return EXCLUDE_PREFIXES.stream().anyMatch(uri::startsWith);
    }

    private static final Set<String> EXCLUDE_PREFIXES = Set.of( //필터를 패스하는 패턴
            "/login",
            "/sign-up",
            "/favicon.ico",
            "/.well-known",
            "/error"
    );
}

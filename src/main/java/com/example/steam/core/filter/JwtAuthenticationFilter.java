package com.example.steam.core.filter;

import com.example.steam.core.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
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


        String token = resolveToken((HttpServletRequest) servletRequest);
        if(jwtProvider.validateToken(token)) {
            Authentication auth = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    //request에서 token 추출
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(StringUtils.hasText(token) && token.startsWith("Bearer")){
            return token.substring(7);
        }
        return null;
    }

    private boolean isExcluded(String uri) {
        return EXCLUDE_PREFIXES.stream().anyMatch(uri::startsWith);
    }

    private static final Set<String> EXCLUDE_PREFIXES = Set.of(
            "/",
            "/login",
            "/sign-up",
            "/favicon.ico",
            "/.well-known",
            "/error"
    );
}

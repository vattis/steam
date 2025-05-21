package com.example.steam.core.jwt;

import com.example.steam.module.member.application.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider {

    private final CustomUserDetailsService userDetailsService;

    public JwtProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //granType + accessToken + refreshToken 을 합쳐서 생성
    public JwtToken generateToken(Authentication authentication)
    {
        long now = System.currentTimeMillis();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        String accessToken = Jwts.builder()   //만료 기한, subject, 권한이 포함되고 시크릿 키로 해싱해서 생성
                .setExpiration(new Date(now+JwtConst.JWT_ACCESS_TOKEN_EXPIRES_IN))
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(key)
                .compact();


        String refreshToken = Jwts.builder()    //중요한 정보 대신 만료 기한만 포함되고 시크릿 키로 해싱해서 생성
                .setExpiration(new Date(now+JwtConst.JWT_REFRESH_TOKEN_EXPIRES_IN))
                .signWith(key)
                .compact();


        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //accessToken을 받아서 인증된 UsernamePasswordAuthenticationToken으로 만들어주는 메서드
    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaim(accessToken);
        if(claims.get("auth") == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    //토큰 검증 메서드
    public boolean validateToken(String token) {
        //parser로 토큰을 분해하는 과정에서 방생하는 exception 확인
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT token", e);
        }catch (ExpiredJwtException e){
            log.info("Expired JWT token", e);
        }catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token", e);
        }catch (IllegalArgumentException e){
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    private Claims parseClaim(String accessToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}

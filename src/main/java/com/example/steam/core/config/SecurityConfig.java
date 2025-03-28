package com.example.steam.core.config;

import com.example.steam.core.filter.JwtAuthenticationFilter;
import com.example.steam.core.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity, JwtProvider jwtProvider) throws Exception {
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable) //기본 인증 로그인 비활성화
                .csrf(AbstractHttpConfigurer::disable) //서버에 인증정보를 보관하지 않기 때문에 csrf 보호 비활성화
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer //세션 사용 안함
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequest)->authorizeRequest
                        .requestMatchers("/", "/login", "/sign-up", "/articles").permitAll()
                        .requestMatchers("/login/test").hasRole("USER")
                        .anyRequest().authenticated())
                .exceptionHandling(a->a.accessDeniedPage("/noAuthorities"))
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

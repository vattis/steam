package com.example.steam.core.jwt;

import lombok.Builder;

@Builder
public class JwtToken {
    String grantType;
    String accessToken;
    String refreshToken;
}

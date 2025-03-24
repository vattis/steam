package com.example.steam.core.jwt;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtToken {
    String grantType;
    String accessToken;
    String refreshToken;
}

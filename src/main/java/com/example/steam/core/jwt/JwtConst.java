package com.example.steam.core.jwt;

public class JwtConst {
    public static long JWT_ACCESS_TOKEN_EXPIRES_IN = 60 * 10 * 1000; //10분
    public static long JWT_REFRESH_TOKEN_EXPIRES_IN = 60 * 60 * 24 * 7 * 1000; //7일
}

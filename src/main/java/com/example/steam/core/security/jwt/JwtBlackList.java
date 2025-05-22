package com.example.steam.core.security.jwt;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtBlackList { //계속 메모리에 토큰이 누적되겠지만, 나중에 redis로 바꿀예정임
    public static Map<String, Long> blackList = new HashMap<>();
    public static void addBlackList(String token, long time) {
        blackList.put(token, time);
    }
    public static boolean isBlackList(String token) {
        Long expire = blackList.get(token);
        if(expire == null || System.currentTimeMillis() > expire){
            blackList.remove(token);
            return false;
        }
        return true;
    }
}

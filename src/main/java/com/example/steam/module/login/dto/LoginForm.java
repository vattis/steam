package com.example.steam.module.login.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginForm {
    private String email;
    private String password;

    public static LoginForm of(String email, String password) {
        return LoginForm.builder()
                .email(email)
                .password(password)
                .build();
    }
}

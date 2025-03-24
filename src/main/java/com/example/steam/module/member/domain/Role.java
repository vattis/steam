package com.example.steam.module.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"), USER("USER");

    private final String label;

    Role(String role) {
        this.label = role;
    }
}

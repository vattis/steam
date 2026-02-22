package com.example.steam.module.gallery.domain;

import com.example.steam.module.member.domain.MemberGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SimpleGalleryDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;

    public static SimpleGalleryDto makeDtoWithMemberGame(MemberGame memberGame) {
        return SimpleGalleryDto.builder()
                .name(memberGame.getProduct().getName())
                .imageUrl(memberGame.getProduct().getImageUrl())
                .build();
    }

    public static SimpleGalleryDto from(Gallery gallery) {
        return SimpleGalleryDto.builder()
                .id(gallery.getId())
                .name(gallery.getProduct().getName())
                .description(gallery.getProduct().getName() + " 갤러리")
                .imageUrl(gallery.getProduct().getImageUrl())
                .build();
    }
}

package com.example.steam.module.comment.domain;

import com.example.steam.module.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name="profile_comment")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE profile_comment SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
public class ProfileComment extends Comment{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member profileMember;

    public static ProfileComment of(Member member, String content, Member profileMember){
        return ProfileComment.builder()
                .member(member)
                .content(content)
                .profileMember(profileMember)
                .createdTime(LocalDateTime.now())
                .build();
    }
}

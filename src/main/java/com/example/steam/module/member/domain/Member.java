package com.example.steam.module.member.domain;

import com.example.steam.module.article.domain.Article;
import com.example.steam.module.comment.domain.Comment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy="member")
    @Builder.Default
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy="member")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
}

package com.example.steam.module.article.domain;

import com.example.steam.module.comment.domain.Comment;
import com.example.steam.module.member.domain.Member;
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
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;


    @OneToMany(mappedBy="article")
    @Builder.Default
    private List<Comment> comments = new ArrayList<Comment>();

    @Column(nullable = false)
    private int likes;


}

package com.example.steam.module.comment.domain;

import com.example.steam.module.article.domain.Article;
import com.example.steam.module.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

//커뮤니티 게시글의 댓글
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleComment {
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Article article;

    @Column(nullable = false)
    private String content;



}

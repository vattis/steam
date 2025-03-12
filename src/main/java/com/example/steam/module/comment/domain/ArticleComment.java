package com.example.steam.module.comment.domain;

import com.example.steam.module.article.domain.Article;
import com.example.steam.module.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

//커뮤니티 게시글의 댓글
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE member SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
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

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false;

}

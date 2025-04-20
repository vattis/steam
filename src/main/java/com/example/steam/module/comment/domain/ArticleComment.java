package com.example.steam.module.comment.domain;

import com.example.steam.module.article.domain.Article;
import com.example.steam.module.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

//커뮤니티 게시글의 댓글
@Entity
@Table(name="article_comment")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE article_comment SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
public class ArticleComment extends Comment{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Article article;
}

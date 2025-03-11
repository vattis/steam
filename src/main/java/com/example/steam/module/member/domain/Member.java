package com.example.steam.module.member.domain;

import com.example.steam.module.article.domain.Article;
import com.example.steam.module.comment.domain.ArticleComment;
import com.example.steam.module.comment.domain.ProductComment;
import com.example.steam.module.friendship.domain.Friendship;
import com.example.steam.module.order.domain.Orders;
import com.example.steam.module.shoppingCart.domain.ShoppingCart;
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

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ArticleComment> articleComments = new ArrayList<>();

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProductComment> productComments = new ArrayList<>();

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Orders> orders = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy="fromMember", cascade = CascadeType.ALL)
    private List<Friendship> friendships = new ArrayList<>();
}

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE member SET deleted = true WHERE id=?") //delete를 사용시, delete=true 업데이트 쿼리를 대신 날린다
@SQLRestriction("deleted is false") //search 사용시 where 절에 delete=false 조건을 추가한다
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

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy="fromMember", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Friendship> friendships = new ArrayList<>();

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false; //soft delete를 위한 field

    @Enumerated(value = EnumType.STRING)
    private Role role;

    public static Member makeSampleWithId(int i){
        Member member= Member.builder()
                            .id((long)i)
                            .nickname("nickName" + i)
                            .email("email" + i)
                            .password("password" + i)
                            .build();
        member.setShoppingCart(ShoppingCart.makeSampleWithId(i, member));
        return member;
    }
    public static Member makeSample(int i){
        Member member= Member.builder()
                .nickname("nickName" + i)
                .email("email" + i)
                .password("password" + i)
                .role(Role.USER)
                .build();
        member.setShoppingCart(ShoppingCart.makeSample(i, member));
        return member;
    }
    public void setShoppingCart(ShoppingCart shoppingCart) {
        if(this.shoppingCart == null){
            this.shoppingCart = shoppingCart;
            shoppingCart.setMember(this);
        }
    }

    public static Member of(String nickname, String email, String password){
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();
    }

    public Member update(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
        return this;
    }
}

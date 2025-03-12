package com.example.steam.module.comment.domain;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


//상품(게임)의 리뷰
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE member SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
public class ProductComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    //리뷰
    @Column
    private String content;

    //리뷰 점수
    @Column
    private float rate;

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false;
}

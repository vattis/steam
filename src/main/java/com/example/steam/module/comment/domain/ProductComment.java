package com.example.steam.module.comment.domain;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;


//상품(게임)의 리뷰
@Entity
@Table(name="product_comment")
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE product_comment SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
public class ProductComment extends Comment{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;

    //리뷰 점수
    @Column
    private Float rate;

    public static ProductComment of(Product product, Member member, String content, Float rate) {
        ProductComment productComment =  ProductComment.builder()
                .product(product)
                .member(member)
                .content(content)
                .rate(rate)
                .createdTime(LocalDateTime.now())
                .build();

        product.addComment(productComment);
        member.getProductComments().add(productComment);
        return productComment;
    }

    public static ProductComment makeSample(Product product, Member member, int num) {
        return ProductComment.of(product, member, "productCommentContent"+num, 4f);
    }
}

package com.example.steam.module.shoppingCart.domain;

import com.example.steam.module.member.domain.Member;
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
@Table(name="shopping_cart")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE shopping_cart SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private Member member;

    @OneToMany(mappedBy="shoppingCart")
    @Builder.Default
    private List<ShoppingCartProduct> shoppingCartProducts = new ArrayList<>();

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false;

    public static ShoppingCart makeSampleWithId(int i, Member member){
        return ShoppingCart.builder()
                .id(i)
                .member(member)
                .build();
    }
    public static ShoppingCart makeSample(int i, Member member){
        return ShoppingCart.builder()
                .member(member)
                .build();
    }
    public void setMember(Member member){
        if(this.member == null){
            this.member = member;
            member.setShoppingCart(this);
        }
    }
}

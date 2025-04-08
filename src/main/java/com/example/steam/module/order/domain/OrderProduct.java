package com.example.steam.module.order.domain;

import com.example.steam.module.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name="order_product")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE order_product SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false;

    @Column(nullable = false)
    private int count;

    public int countUp(int cnt){
        count+=cnt;
        order.calcTotalPrice();
        return count;
    }
    public int countDown(int cnt){
        count -= cnt;
        order.calcTotalPrice();
        return count;
    }


    public static OrderProduct of(Orders order, Product product, int count){
        OrderProduct orderProduct = OrderProduct.builder()
                .order(order)
                .product(product)
                .count(count)
                .build();
        order.addOrderProduct(orderProduct);
        return orderProduct;
    }
}

package com.example.steam.module.discount.domain;

import com.example.steam.module.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql="UPDATE discount SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name= "product_id")
    private Product product;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private int discountRate;

    @Column
    private int discountPrice;

    @Column
    private boolean active;

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false;

    public static Discount of(Product product, LocalDateTime startTime, LocalDateTime endTime, int discountRate, int discountPrice){
        Discount discount = Discount.builder()
                .startTime(startTime)
                .endTime(endTime)
                .discountRate(discountRate)
                .discountPrice(discountPrice).build();
        product.assignDiscount(discount);
        return discount;
    }

    public static Discount makeSample(int num, Product product){
        Random random = new Random();
        return Discount.of(product, LocalDateTime.now(), LocalDateTime.now().plusDays(num), random.nextInt(100), num);
    }
    public boolean isValid(){
        LocalDateTime now = LocalDateTime.now();
        return startTime.isAfter(now) && endTime.isBefore(now);
    }
    public void assignProduct(Product product){
        product.assignDiscount(this);
        this.product = product;
    }
}

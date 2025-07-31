package com.example.steam.module.gallery.domain;

import com.example.steam.module.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    public static Gallery makeSample(Product product){
        return Gallery.builder()
                .product(product)
                .build();
    }

    public static Gallery of(Product product){
        return Gallery.builder()
                .product(product)
                .build();
    }
}

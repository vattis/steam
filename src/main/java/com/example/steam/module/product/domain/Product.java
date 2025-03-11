package com.example.steam.module.product.domain;

import com.example.steam.module.comment.domain.ProductComment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private int price;

    @OneToMany(mappedBy="product")
    @Builder.Default
    private List<ProductComment> productComments = new ArrayList<>();
}

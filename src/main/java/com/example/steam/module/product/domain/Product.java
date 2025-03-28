package com.example.steam.module.product.domain;

import com.example.steam.module.comment.domain.ProductComment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE product SET deleted = true WHERE id=?")
@SQLRestriction("deleted is false")
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

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false;
}

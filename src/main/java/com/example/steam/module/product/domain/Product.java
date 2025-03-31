package com.example.steam.module.product.domain;

import com.example.steam.module.comment.domain.ProductComment;
import com.example.steam.module.company.domain.Company;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Company company;

    @OneToMany(mappedBy="product")
    @Builder.Default
    private List<ProductComment> productComments = new ArrayList<>();

    @Column(name="deleted", nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean deleted = false;

    @Column
    private Float rate;

    @Column(nullable = false)
    private int downloadNum;

    public static Product makeSample(int num, Company company){
        return Product.of("name"+num, 1000*num, company);
    }

    public static Product of(String name, int price, Company company){
        Product product = Product.builder()
                .name(name)
                .price(price)
                .company(company)
                .downloadNum(0)
                .build();
        company.addProduct(product);
        return product;
    }

    public void addComment(ProductComment comment){
        if(comment.getRate() != null){  //평점 업데이트
            if(rate == null){
                rate = 0f;
            }
            int size = this.productComments.size();
            rate = (rate*size+comment.getRate())/(size+1);
        }
        productComments.add(comment);
    }

}

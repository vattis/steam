package com.example.steam.module.product.dto;

import com.example.steam.module.discount.dto.SimpleDiscountDto;
import com.example.steam.module.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleProductBannerDto {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private SimpleDiscountDto discount;

    public SimpleProductBannerDto(Long id, String name, Integer price, String imageUrl, Long discountId, LocalDateTime startTime, LocalDateTime endTime, int discountRate, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discount = SimpleDiscountDto.of(discountId, id, startTime, endTime, discountRate, active);
    }

    public static SimpleProductBannerDto of(Long id, String name, Integer price, String imageUrl, SimpleDiscountDto discount) {
        return SimpleProductBannerDto.builder()
                .id(id)
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .discount(discount)
                .build();
    }
    public static SimpleProductBannerDto from(Product product){
        return SimpleProductBannerDto.of(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), SimpleDiscountDto.from(product.getDiscount()));
    }
    public int getDiscountPrice() {
        return price*((100-discount.getDiscountRate())/100);
    }
}

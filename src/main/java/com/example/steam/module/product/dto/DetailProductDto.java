package com.example.steam.module.product.dto;

import com.example.steam.module.comment.dto.ProductCommentDto;
import com.example.steam.module.discount.dto.SimpleDiscountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@Slf4j
@Builder
@Getter
@AllArgsConstructor
public class DetailProductDto {
    private Long id;
    private String name;
    private String companyName;
    private Long companyId;
    private SimpleDiscountDto discountDto;
    private String imageUrl;
}

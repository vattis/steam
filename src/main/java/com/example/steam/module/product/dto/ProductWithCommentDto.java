package com.example.steam.module.product.dto;

import com.example.steam.module.comment.dto.ProductCommentDto;
import org.springframework.data.domain.Page;

public class ProductWithCommentDto {
    private DetailProductDto detailProductDto;
    private Page<ProductCommentDto> productCommentDtos;
}

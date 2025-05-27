package com.example.steam.module.discount.application;

import com.example.steam.module.discount.domain.Discount;
import com.example.steam.module.product.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class DiscountService {
    public Discount makeDiscount(Product product, LocalDateTime startTime, LocalDateTime endTime, int discountRate, int discountPrice){
        return Discount.of(startTime, endTime, discountRate, discountPrice);
    }
}

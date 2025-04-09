package com.example.steam.module.order.application;

import com.example.steam.module.order.domain.OrderProduct;
import com.example.steam.module.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;

    private int changeCount(Long orderProductId, int cnt){
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(NoSuchElementException::new);
        orderProduct.changeCount(cnt);
        return cnt;
    }
}

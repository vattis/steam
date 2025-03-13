package com.example.steam.module.shoppingCart.repository;

import com.example.steam.module.shoppingCart.domain.ShoppingCartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, Long> {
}

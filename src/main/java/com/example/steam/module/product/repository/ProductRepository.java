package com.example.steam.module.product.repository;

import com.example.steam.module.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByOrderByDownloadNum(Pageable pageable);
    Page<Product> findAllByNameContaining(String searchWord, Pageable pageable);
    Page<Product> findAllByCompanyNameContaining(String searchWord, Pageable pageable);
    Page<Product> findAllByNameOrCompanyNameContaining(String searchWord, Pageable pageable);
}

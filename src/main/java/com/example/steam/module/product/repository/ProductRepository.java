package com.example.steam.module.product.repository;

import com.example.steam.module.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByOrderByDownloadNum(Pageable pageable);

    Page<Product> findAllByNameContaining(String searchWord, Pageable pageable);

    Page<Product> findAllByCompanyNameContaining(String searchWord, Pageable pageable);

    @Query(value = "select distinct p from Product p join p.company c where p.name like concat('%', :searchWord, '%') or c.name like concat('%', :searchWord, '%')")
    Page<Product> findAllByNameOrCompanyNameContaining(@Param("searchWord") String searchWord, Pageable pageable);

    Page<Product> findAllByCompanyId(Long companyId, Pageable pageable);
}

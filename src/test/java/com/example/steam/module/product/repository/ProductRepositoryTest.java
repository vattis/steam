package com.example.steam.module.product.repository;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.company.domain.Company;
import com.example.steam.module.company.repository.CompanyRepository;
import com.example.steam.module.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired private CompanyRepository companyRepository;
    @Autowired private ProductRepository productRepository;


    @BeforeEach
    void setUp() {
        List<Company> companies = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            companies.add(Company.makeSample(i));
        }
        for(int i = 1; i <= 100; i++){
            products.add(Product.makeSample(i, companies.get(i%10)));
        }
        companyRepository.saveAll(companies);
        productRepository.saveAll(products);
    }

    @Test
    void findAllByOrderByDownloadNum() {
        //given
        PageRequest pageRequest = PageRequest.of(0, PageConst.PRODUCT_PAGE_SIZE);

        //when
        Page<Product> productPage1 = productRepository.findAllByOrderByDownloadNum(pageRequest);

        //then
        assertThat(productPage1.getTotalElements()).isEqualTo(100);
    }

    @Test
    void findAllByNameContaining() {
        //given
        PageRequest pageRequest = PageRequest.of(0, PageConst.PRODUCT_PAGE_SIZE);
        String nameSearchWord = "name3";

        //when
        Page<Product> productPage2 = productRepository.findAllByNameContaining(nameSearchWord, pageRequest);

        //then
        assertThat(productPage2.getTotalElements()).isEqualTo(11);
    }

    @Test
    void findAllByCompanyNameContaining() {
        //given
        PageRequest pageRequest = PageRequest.of(0, PageConst.PRODUCT_PAGE_SIZE);
        String companyNameSearchWord = "panyName4";

        //when
        Page<Product> productPage3 = productRepository.findAllByCompanyNameContaining(companyNameSearchWord, pageRequest);

        //then
        assertThat(productPage3.getTotalElements()).isEqualTo(10);
    }

    @Test
    void findAllByNameOrCompanyNameContaining() {
        //given
        PageRequest pageRequest = PageRequest.of(0, PageConst.PRODUCT_PAGE_SIZE);
        String allSearchWord = "ame5";

        //when
        Page<Product> productPage4 = productRepository.findAllByNameOrCompanyNameContaining(allSearchWord, pageRequest);

        //then
        assertThat(productPage4.getTotalElements()).isEqualTo(20);
    }
}
package com.example.steam.module.product.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.company.domain.Company;
import com.example.steam.module.product.domain.Product;
import com.example.steam.module.product.domain.ProductSearch;
import com.example.steam.module.product.domain.ProductSearchTag;
import com.example.steam.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    //게임 등록
    public Product save(String name, int price, Company company){
        Product product = Product.of(name, price, company);
        return productRepository.save(product);
    }

    //전체 조회 및 검색
    public Page<Product> search(ProductSearch productSearch){
        PageRequest pageRequest = PageRequest.of(0, PageConst.ARTICLE_PAGE_SIZE);
        if(productSearch.getSearchWord() == null || productSearch.getSearchWord().isEmpty()){ //전체 조회
            return productRepository.findAllByOrderByDownloadNum(pageRequest);
        }else if(productSearch.getSearchTag() == ProductSearchTag.NAME){ //게임 이름 조회
            return productRepository.findAllByNameContaining(productSearch.getSearchWord(), pageRequest);
        }else if(productSearch.getSearchTag() == ProductSearchTag.COMPANY){ //게임 개발사 조회
            return productRepository.findAllByCompanyNameContaining(productSearch.getSearchWord(), pageRequest);
        }else if(productSearch.getSearchTag() == ProductSearchTag.ALL){ //게임 이름 또는 개발사 조회
            return productRepository.findAllByNameOrCompanyNameContaining(productSearch.getSearchWord(), pageRequest);
        }
        return null;
    }

    //게임 한개 조회
    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    //게임 삭제
    public void deleteById(Long id){
        productRepository.deleteById(id);
    }
}

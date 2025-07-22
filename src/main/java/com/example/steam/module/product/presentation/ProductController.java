package com.example.steam.module.product.presentation;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.member.repository.MemberRepository;
import com.example.steam.module.product.application.ProductService;
import com.example.steam.module.product.domain.Product;
import com.example.steam.module.product.domain.ProductSearch;
import com.example.steam.module.product.domain.ProductSearchTag;
import com.example.steam.module.product.dto.SimpleProductBannerDto;
import com.example.steam.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/shop/product")
    String gotoShop(Model model){
        Pageable pageable = PageRequest.of(0, PageConst.PRODUCTS_BANNER_SIZE);
        Page<SimpleProductBannerDto> discountProducts = productService.findDiscountProductBanner(pageable);
        Page<SimpleProductBannerDto> popularProducts = productService.findPopularProductBanner(pageable);
        model.addAttribute("discountProducts", discountProducts);
        model.addAttribute("popularProducts", popularProducts);
        return "/shop";
    }
    @GetMapping("/product/{productId}")
    String gotoProduct(Model model, @PathVariable("productId") Long productId){
        Product product = productService.findById(productId);
        model.addAttribute("game", product);
        return "/product";
    }
    @GetMapping("/product/search")
    String searchProduct(Model model, @RequestParam("tag") String searchTag, @RequestParam("searchWord") String searchWord){
        log.info("controller: search tag={}, searchWord={}", searchTag, searchWord);
        ProductSearch productSearch = ProductSearch.of(ProductSearchTag.makeTag(searchTag), searchWord);
        Page<SimpleProductBannerDto> products = productService.search(productSearch);
        model.addAttribute("searchResults", products);
        return "/searchProduct";
    }
}

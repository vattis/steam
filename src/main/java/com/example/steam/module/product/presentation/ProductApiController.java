package com.example.steam.module.product.presentation;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.comment.application.ProductCommentService;
import com.example.steam.module.comment.dto.ProductCommentDto;
import com.example.steam.module.product.application.ProductService;
import com.example.steam.module.product.domain.ProductSearch;
import com.example.steam.module.product.domain.ProductSearchTag;
import com.example.steam.module.product.dto.DetailProductDto;
import com.example.steam.module.product.dto.SimpleProductBannerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductApiController {
    private final ProductService productService;
    private final ProductCommentService productCommentService;

    @GetMapping("/shop/products")
    public ResponseEntity<Map<String, Object>> getShopProducts(
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo) {

        Pageable pageable;
        if (filter == null) {
            pageable = PageRequest.of(pageNo, PageConst.PRODUCTS_BANNER_SIZE);
        } else if (filter.equals("popular")) {
            pageable = PageRequest.of(pageNo, PageConst.PRODUCT_PAGE_SIZE);
        } else if (filter.equals("discount")) {
            pageable = PageRequest.of(pageNo, PageConst.PRODUCT_PAGE_SIZE);
        } else {
            pageable = PageRequest.of(pageNo, PageConst.PRODUCTS_BANNER_SIZE);
        }

        Page<SimpleProductBannerDto> discountProducts = productService.findDiscountProductBanner(pageable);
        Page<SimpleProductBannerDto> popularProducts = productService.findTop5PopularProductBanner();

        Map<String, Object> response = new HashMap<>();
        response.put("discountProducts", discountProducts);
        response.put("popularProducts", popularProducts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> getProduct(
            @PathVariable("productId") Long productId,
            @RequestParam(defaultValue = "0") int pageNo) {

        DetailProductDto productDto = DetailProductDto.from(productService.findById(productId));
        Page<ProductCommentDto> comments = productCommentService
                .findProductCommentByProductId(productId, pageNo)
                .map(ProductCommentDto::from);

        Map<String, Object> response = new HashMap<>();
        response.put("product", productDto);
        response.put("comments", comments);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/search")
    public ResponseEntity<Page<SimpleProductBannerDto>> searchProducts(
            @RequestParam(name = "tag", required = false) String searchTag,
            @RequestParam(name = "searchWord", required = false) String searchWord,
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo) {

        ProductSearch productSearch = ProductSearch.of(ProductSearchTag.makeTag(searchTag), searchWord);
        Page<SimpleProductBannerDto> products = productService.search(productSearch, pageNo);

        return ResponseEntity.ok(products);
    }
}

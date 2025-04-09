package com.example.steam.module.shoppingCart.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import com.example.steam.module.order.domain.OrderProduct;
import com.example.steam.module.order.domain.Orders;
import com.example.steam.module.order.repository.OrdersRepository;
import com.example.steam.module.product.domain.Product;
import com.example.steam.module.product.repository.ProductRepository;
import com.example.steam.module.shoppingCart.domain.ShoppingCart;
import com.example.steam.module.shoppingCart.domain.ShoppingCartProduct;
import com.example.steam.module.shoppingCart.repository.ShoppingCartProductRepository;
import com.example.steam.module.shoppingCart.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;

    //장바구니 목록 조회
    public Page<ShoppingCartProduct> getShoppingCartProducts(Long memberId){
        PageRequest pageRequest = PageRequest.of(0, PageConst.SHOPPING_CART_PRODUCT_PAGE_SIZE);
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        return shoppingCartProductRepository.findAllByShoppingCartId(member.getShoppingCart().getId(), pageRequest);
    }

    //장바구니 상품 삭제
    public void removeShoppingCartProduct(Long shoppingCartProductId){
        shoppingCartProductRepository.deleteById(shoppingCartProductId);
    }

    //장바구니 상품 추가
    public void addShoppingCartProduct(Long memberId, Long productId){
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        member.getShoppingCart().addShoppingCartProduct(product);
    }

    //장바구니를 주문으로 변경
    public Orders makeShoppingCartToOrder(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        ShoppingCart shoppingCart = member.getShoppingCart();
        return ordersRepository.save(shoppingCart.toOrders());
    }
}

package com.example.steam.module.shoppingCart.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import com.example.steam.module.order.domain.Orders;
import com.example.steam.module.order.repository.OrdersRepository;
import com.example.steam.module.product.application.ProductService;
import com.example.steam.module.product.domain.Product;
import com.example.steam.module.product.repository.ProductRepository;
import com.example.steam.module.member.domain.MemberGame;
import com.example.steam.module.member.repository.MemberGameRepository;
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

import java.util.List;
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
    private final MemberGameRepository memberGameRepository;
    private final MemberService memberService;
    private final ProductService productService;

    //장바구니 목록 조회
    public Page<ShoppingCartProduct> getShoppingCartProducts(Long memberId, int pageNo){
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        PageRequest pageRequest = PageRequest.of(pageNo, PageConst.SHOPPING_CART_PRODUCT_PAGE_SIZE);
        return shoppingCartProductRepository.findAllByShoppingCartId(member.getShoppingCart().getId(), pageRequest);
    }

    //id로 장바구니 상품 찾기
    public ShoppingCartProduct findById(Long shoppingCartProductId){
        return shoppingCartProductRepository.findById(shoppingCartProductId).orElseThrow(NoSuchElementException::new);
    }

    //장바구니 상품 삭제
    public void removeShoppingCartProduct(ShoppingCartProduct shoppingCartProduct, Long memberId){
        if(!shoppingCartProduct.getShoppingCart().getMember().getId().equals(memberId)){
            log.info("잘못된 ShoppingCartProduct 삭제 요청::사용자 불일치");
            return;
        }
        shoppingCartProductRepository.delete(shoppingCartProduct);
    }

    //장바구니 상품 추가
    public void addShoppingCartProduct(Long memberId, Long productId){
        if(shoppingCartProductRepository.existsByShoppingCartIdAndProductId(memberId, productId)){
            log.info("잘못된 ShoppingCartProduct 추가 요청::중복된 product 요청");
            return;
        }
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        ShoppingCartProduct shoppingCartProduct = ShoppingCartProduct.of(member.getShoppingCart(), product);
        member.getShoppingCart().addShoppingCartProduct(shoppingCartProduct);
        shoppingCartProductRepository.save(shoppingCartProduct);
    }

    //장바구니를 주문으로 변경
    public Orders makeShoppingCartToOrder(Member member){
        ShoppingCart shoppingCart = member.getShoppingCart();

        // 장바구니 상품 리스트 복사 (toOrders에서 clear되기 전에)
        List<ShoppingCartProduct> cartProducts = new java.util.ArrayList<>(shoppingCart.getShoppingCartProducts());

        // 장바구니의 각 상품을 회원 라이브러리에 추가
        for (ShoppingCartProduct cartProduct : cartProducts) {
            MemberGame memberGame = MemberGame.of(cartProduct.getProduct(), member);
            memberGameRepository.save(memberGame);
        }

        // 주문 생성 (장바구니 비우기 포함)
        Orders order = shoppingCart.toOrders();
        Orders savedOrder = ordersRepository.save(order);

        // 장바구니 상품 DB에서 삭제
        shoppingCartProductRepository.deleteAll(cartProducts);

        return savedOrder;
    }
}

package com.example.steam.module.shoppingCart.presentation;

import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.dto.SimpleMemberDto;
import com.example.steam.module.member.repository.MemberRepository;
import com.example.steam.module.order.domain.Orders;
import com.example.steam.module.shoppingCart.application.ShoppingCartService;
import com.example.steam.module.shoppingCart.domain.ShoppingCartProduct;
import com.example.steam.module.shoppingCart.dto.SimpleShoppingCartProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartApiController {
    private final ShoppingCartService shoppingCartService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 장바구니 목록 조회
    @GetMapping("/shoppingCart")
    public ResponseEntity<Page<SimpleShoppingCartProductDto>> getCart(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        Page<SimpleShoppingCartProductDto> cart = shoppingCartService
                .getShoppingCartProducts(member.getId(), pageNo)
                .map(SimpleShoppingCartProductDto::from);

        return ResponseEntity.ok(cart);
    }

    // 장바구니에 상품 추가
    @PostMapping("/shoppingCart/{productId}")
    public ResponseEntity<Map<String, String>> addToCart(
            @PathVariable Long productId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        shoppingCartService.addShoppingCartProduct(member.getId(), productId);

        return ResponseEntity.ok(Map.of("message", "장바구니에 추가되었습니다."));
    }

    // 장바구니에서 상품 삭제
    @DeleteMapping("/shoppingCart/{cartItemId}")
    public ResponseEntity<Map<String, String>> removeFromCart(
            @PathVariable Long cartItemId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        ShoppingCartProduct item = shoppingCartService.findById(cartItemId);
        shoppingCartService.removeShoppingCartProduct(item, member.getId());

        return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
    }

    // 결제 (주문 생성)
    @PostMapping("/order/checkout")
    public ResponseEntity<Map<String, Object>> checkout(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(NoSuchElementException::new);

        Orders order = shoppingCartService.makeShoppingCartToOrder(member);

        return ResponseEntity.ok(Map.of(
                "message", "결제가 완료되었습니다.",
                "orderId", order.getId()
        ));
    }
}

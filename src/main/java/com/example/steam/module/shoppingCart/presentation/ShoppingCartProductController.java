package com.example.steam.module.shoppingCart.presentation;

import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.shoppingCart.application.ShoppingCartService;
import com.example.steam.module.shoppingCart.domain.ShoppingCartProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartProductController {
    private final ShoppingCartService shoppingCartService;
    private final MemberService memberService;
    @DeleteMapping("/shoppingCartProduct/{shoppingCartProductId}")
    public String deleteShoppingCartProduct(@PathVariable("shoppingCartProductId") Long shoppingCartProductId, Principal principal){
        Member member = memberService.findMemberByEmail(principal.getName());
        ShoppingCartProduct shoppingCartProduct = shoppingCartService.findById(shoppingCartProductId);
        shoppingCartService.removeShoppingCartProduct(shoppingCartProduct, member);
        return "redirect:/shoppingCart";
    }
}

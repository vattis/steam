package com.example.steam.module.comment.presentation;

import com.example.steam.module.comment.application.ProductCommentService;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductCommentController {
    private final ProductCommentService productCommentService;
    private final MemberService memberService;

    @PostMapping("/productComment/{productId}")
    String postProductComment(@PathVariable("productId") Long productId,
                              @RequestParam("content") String content,
                              @RequestParam("rating") Float rate,
                              Principal principal){
        Member member = memberService.findMemberByEmail(principal.getName());
        productCommentService.makeProductComment(member, productId, content, rate);
        return "redirect:/product/"+productId;
    }
}

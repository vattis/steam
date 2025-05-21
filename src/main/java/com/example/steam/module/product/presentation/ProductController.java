package com.example.steam.module.product.presentation;

import com.example.steam.module.member.domain.MemberUserDetails;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final MemberRepository memberRepository;

    @GetMapping("/")
    String gotoMain(@AuthenticationPrincipal MemberUserDetails userDetails, Model model){
        return "/main";
    }
}

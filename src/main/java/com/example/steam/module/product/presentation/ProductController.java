package com.example.steam.module.product.presentation;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.domain.MemberUserDetails;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final MemberRepository memberRepository;

    @GetMapping("/")
    String gotoMain(@AuthenticationPrincipal MemberUserDetails userDetails, Model model){
        if(userDetails != null){
            model.addAttribute("memberId", userDetails.getId());
        }
        return "/main";
    }
}

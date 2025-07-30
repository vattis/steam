package com.example.steam.module.gallery.presentation;

import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.domain.MemberGame;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GalleryController {
    private final MemberRepository memberRepository;
    @GetMapping("/galleries")
    public String togoGalleryList(Model model, Principal principal){
        if(principal != null){
            Member member = memberRepository.findByEmail(principal.getName()).orElseThrow(NoSuchElementException::new);
            List<MemberGame> memberGames = member.getMemberGames();
        }
        return "/gallery-list";
    }
}

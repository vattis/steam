package com.example.steam.module.gallery.presentation;

import com.example.steam.module.gallery.application.GalleryService;
import com.example.steam.module.gallery.domain.SimpleGalleryDto;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.domain.MemberGame;
import com.example.steam.module.member.dto.SimpleMemberGameDto;
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
    private final MemberService memberService;
    private final GalleryService galleryService;
    @GetMapping("/galleries")
    public String togoGalleryList(Model model, Principal principal){
        if(principal != null){
            Member member = memberService.findMemberByEmail(principal.getName());
            List<SimpleGalleryDto> ownedGalleries = member.getMemberGames().stream().map(SimpleGalleryDto::makeDtoWithMemberGame).toList();
            List<SimpleGalleryDto> galleries = galleryService.findAllGallery().stream().map(SimpleGalleryDto::from).toList();

            model.addAttribute("ownedGalleries", ownedGalleries);
            model.addAttribute("galleries", galleries);
        }
        return "/gallery-list";
    }
}

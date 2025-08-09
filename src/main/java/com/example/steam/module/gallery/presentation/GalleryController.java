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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    //갤러리 목록으로 이동
    @GetMapping("/galleries")
    public String togoGalleryList(@RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo, Model model, Principal principal){
        if(principal != null){
            Member member = memberService.findMemberByEmail(principal.getName());
            List<SimpleGalleryDto> ownedGalleries = member.getMemberGames().stream().map(SimpleGalleryDto::makeDtoWithMemberGame).toList();
            Page<SimpleGalleryDto> galleries = galleryService.findAllGallery(pageNo).map(SimpleGalleryDto::from);

            model.addAttribute("ownedGalleries", ownedGalleries);
            model.addAttribute("galleries", galleries);
        }
        return "/gallery-list";
    }

    //갤러리 검색
    @GetMapping("/gallery/search")
    public String search(@RequestParam("searchWord") String searchWord,
                         @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
                         Model model){
        Page<SimpleGalleryDto> galleryDtos = galleryService.search(searchWord, pageNo).map(SimpleGalleryDto::from);
        System.out.println(galleryDtos.getTotalElements() + "@@@@@@@@@@@@@@@@@@@@@@@");
        model.addAttribute("galleries", galleryDtos);
        model.addAttribute("searchWord", searchWord);
        return "/gallery-search";
    }
}

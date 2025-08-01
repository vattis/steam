package com.example.steam.module.article.presentation;

import com.example.steam.module.article.application.ArticleService;
import com.example.steam.module.article.dto.ArticleDto;
import com.example.steam.module.article.dto.ArticleWriteForm;
import com.example.steam.module.gallery.application.GalleryService;
import com.example.steam.module.gallery.domain.Gallery;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final MemberService memberService;
    private final GalleryService galleryService;

    //해당 갤러리로 이동
    @GetMapping("/article/{galleryName}")
    public String gotoGallery(@PathVariable String galleryName,
                              @RequestParam(required = false, defaultValue = "0") int pageNo,
                              Model model) {
        Gallery gallery = galleryService.findGalleryWithProductName(galleryName);
        Page<ArticleDto> articlePage = articleService.findAllByGalleryId(gallery.getId(), pageNo).map(ArticleDto::from);
        model.addAttribute("articleDto", articlePage);
        return "/gallery";
    }

    //개시물 작성 폼으로 이동
    @GetMapping("/article/write")
    public String gotoWrite(Model model) {
        return "/writeArticle";
    }

    //개시물 작성
    @PostMapping("article/{galleryId}/write")
    public String postArticle(@ModelAttribute ArticleWriteForm articleWriteForm, Principal principal){
        if(principal == null){ //비로그인 시 로그인 화면으로
            return "redirect:/login";
        }
        Member member = memberService.findMemberByEmail(principal.getName());
        articleService.saveArticle(articleWriteForm, member);
        return "redirect:/article/"+ articleWriteForm.getGalleryId();
    }



}

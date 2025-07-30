package com.example.steam.module.article.presentation;

import com.example.steam.module.article.application.ArticleService;
import com.example.steam.module.article.dto.ArticleDto;
import com.example.steam.module.article.dto.ArticleWriteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/article/{galleryId}")
    public String gotoGallery(@PathVariable long galleryId,
                              @RequestParam(required = false, defaultValue = "0") int pageNo,
                              Model model) {
        Page<ArticleDto> articlePage = articleService.findAllByGalleryId(galleryId, pageNo).map(ArticleDto::from);
        model.addAttribute("articlePage", articlePage);
        return "/gallery";
    }

    //개시물 작성 폼으로 이동
    @GetMapping("/article/write")
    public String gotoWrite(Model model) {
        return "/writeArticle";
    }

    //개시물 작성
    @PostMapping("article/{galleryId}/write")
    public String postArticle(@ModelAttribute ArticleWriteForm articleWriteForm){
        articleService.saveArticle(articleWriteForm);
        return "redirect:/article/"+ articleWriteForm.getGalleryId();
    }


}

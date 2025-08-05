package com.example.steam.module.article.presentation;

import com.example.steam.module.article.application.ArticleService;
import com.example.steam.module.article.domain.Article;
import com.example.steam.module.article.domain.ArticleSearch;
import com.example.steam.module.article.domain.ArticleSearchTag;
import com.example.steam.module.article.dto.ArticleDto;
import com.example.steam.module.article.dto.ArticleWriteForm;
import com.example.steam.module.article.dto.DetailArticleDto;
import com.example.steam.module.article.dto.DetailArticleWithCommentDto;
import com.example.steam.module.comment.application.ArticleCommentService;
import com.example.steam.module.comment.dto.ArticleCommentDto;
import com.example.steam.module.gallery.application.GalleryService;
import com.example.steam.module.gallery.domain.Gallery;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final ArticleCommentService articleCommentService;

    //해당 갤러리로 이동
    @GetMapping("/gallery/{galleryName}")
    public String gotoGallery(@PathVariable String galleryName,
                              @RequestParam(required = false, defaultValue = "0") int pageNo,
                              Model model) {
        Gallery gallery = galleryService.findGalleryWithProductName(galleryName);
        Page<ArticleDto> articlePage = articleService.findAllByGalleryId(gallery.getId(), pageNo).map(ArticleDto::from);
        model.addAttribute("articleDto", articlePage);
        model.addAttribute("galleryId", gallery.getId());
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

    //게시물 화면 이동
    @GetMapping("/article/{articleId}")
    public String gotoArticle(@PathVariable("articleId") Long articleId,
                              @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
                              Model model){
        Article article = articleService.findArticle(articleId);
        Page<ArticleCommentDto> articleCommentDtoPage = articleCommentService.findArticleCommentByArticleId(articleId, pageNo).map(ArticleCommentDto::from);
        DetailArticleWithCommentDto detailArticleWithCommentDto = DetailArticleWithCommentDto.of(DetailArticleDto.from(article), articleCommentDtoPage);
        model.addAttribute("articleCommentDto", detailArticleWithCommentDto);
        return "/article";
    }

    //게시물 검색
    @GetMapping("/gallery/{galleryId}/articles")
    public String searchArticles(@PathVariable("galleryId")Long galleryId,
                                 @RequestParam("tag") String tag,
                                 @RequestParam("searchWord") String searchWord,
                                 @RequestParam(required = false, defaultValue = "0") int pageNo,
                                 Model model){
        ArticleSearchTag articleSearchTag = getArticleSearchTag(tag);
        ArticleSearch articleSearch = ArticleSearch.of(articleSearchTag, searchWord);
        Page<ArticleDto> articleDtos = articleService.findAllBySearchWord(galleryId, articleSearch).map(ArticleDto::from);
        model.addAttribute("articleDtos", articleDtos);
        model.addAttribute("galleryId", galleryId);
        model.addAttribute("searchWord", searchWord);
        return "search-article";
    }

    private static ArticleSearchTag getArticleSearchTag(String tag) {
        return switch (tag) {
            case "ALL" -> ArticleSearchTag.ALL;
            case "TITLE" -> ArticleSearchTag.TITLE;
            case "CONTENT" -> ArticleSearchTag.CONTENT;
            case "MEMBER" -> ArticleSearchTag.NICKNAME;
            case "COMMENT" -> ArticleSearchTag.COMMENT;
            default -> null;
        };
    }


}

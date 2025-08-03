package com.example.steam.module.comment.presentation;

import com.example.steam.module.article.application.ArticleService;
import com.example.steam.module.article.domain.Article;
import com.example.steam.module.comment.application.ArticleCommentService;
import com.example.steam.module.comment.domain.ArticleComment;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ArticleCommentController {
    private final MemberService memberService;
    private final ArticleService articleService;
    private final ArticleCommentService articleCommentService;

    @PostMapping("/articleComment")
    public String postArticleComment(@RequestParam("articleId") Long articleId, @RequestParam("commentContent") String articleCommentContent, Principal principal){
        Member member = memberService.findMemberByEmail(principal.getName());
        Article article = articleService.findArticle(articleId);
        articleCommentService.makeArticleComment(member, article, articleCommentContent);
        return "redirect:/article/" + articleId;
    }
}

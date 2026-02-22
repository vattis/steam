package com.example.steam.module.article.presentation;

import com.example.steam.module.article.application.ArticleService;
import com.example.steam.module.article.domain.Article;
import com.example.steam.module.article.domain.ArticleSearch;
import com.example.steam.module.article.domain.ArticleSearchTag;
import com.example.steam.module.article.dto.ArticleDto;
import com.example.steam.module.article.dto.ArticleWriteForm;
import com.example.steam.module.article.dto.DetailArticleDto;
import com.example.steam.module.comment.application.ArticleCommentService;
import com.example.steam.module.comment.dto.ArticleCommentDto;
import com.example.steam.module.gallery.application.GalleryService;
import com.example.steam.module.gallery.domain.Gallery;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.dto.SimpleMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ArticleApiController {
    private final ArticleService articleService;
    private final MemberService memberService;
    private final GalleryService galleryService;
    private final ArticleCommentService articleCommentService;

    // 게시글 상세 조회
    @GetMapping("/article/{articleId}")
    public ResponseEntity<Map<String, Object>> getArticle(
            @PathVariable("articleId") Long articleId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "view", required = false, defaultValue = "true") boolean view) {

        Article article = view ? articleService.findArticleWithView(articleId) : articleService.findArticle(articleId);
        Page<ArticleCommentDto> comments = articleCommentService
                .findArticleCommentByArticleId(articleId, pageNo)
                .map(ArticleCommentDto::from);

        Map<String, Object> response = new HashMap<>();
        response.put("article", DetailArticleDto.from(article));
        response.put("comments", comments);

        return ResponseEntity.ok(response);
    }

    // 게시글 검색
    @GetMapping("/article/search")
    public ResponseEntity<Page<ArticleDto>> searchArticles(
            @RequestParam("galleryId") Long galleryId,
            @RequestParam(name = "tag", required = false, defaultValue = "ALL") String tag,
            @RequestParam(name = "searchWord", required = false, defaultValue = "") String searchWord,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo) {

        ArticleSearchTag articleSearchTag = getArticleSearchTag(tag);
        ArticleSearch articleSearch = ArticleSearch.of(articleSearchTag, searchWord);
        Gallery gallery = galleryService.findById(galleryId);

        Page<ArticleDto> articles = articleService
                .findAllBySearchWord(gallery, articleSearch)
                .map(ArticleDto::from);

        return ResponseEntity.ok(articles);
    }

    // 게시글 작성
    @PostMapping("/article")
    public ResponseEntity<Map<String, Object>> createArticle(
            @RequestBody ArticleWriteForm articleWriteForm,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        Article article = articleService.saveArticle(articleWriteForm, member.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("articleId", article.getId());
        response.put("message", "게시글이 작성되었습니다.");

        return ResponseEntity.ok(response);
    }

    // 게시글 삭제
    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<Map<String, String>> deleteArticle(
            @PathVariable("articleId") Long articleId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        articleService.deleteArticle(articleId, member.getId());

        return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
    }

    // 게시글 댓글 작성
    @PostMapping("/article/{articleId}/comment")
    public ResponseEntity<Map<String, String>> addComment(
            @PathVariable("articleId") Long articleId,
            @RequestBody Map<String, String> body,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        String content = body.get("content");
        Article article = articleService.findArticle(articleId);
        articleCommentService.makeArticleComment(member.getId(), article, content);

        return ResponseEntity.ok(Map.of("message", "댓글이 작성되었습니다."));
    }

    // 게시글 댓글 삭제
    @DeleteMapping("/article/comment/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable("commentId") Long commentId,
            @RequestParam("articleId") Long articleId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto member = memberService.findMemberDtoByEmail(principal.getName());
        var articleComment = articleCommentService.findById(commentId);
        articleCommentService.deleteArticleComment(articleComment, member.getId());

        return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
    }

    private static ArticleSearchTag getArticleSearchTag(String tag) {
        return switch (tag) {
            case "ALL" -> ArticleSearchTag.ALL;
            case "TITLE" -> ArticleSearchTag.TITLE;
            case "CONTENT" -> ArticleSearchTag.CONTENT;
            case "MEMBER" -> ArticleSearchTag.NICKNAME;
            case "COMMENT" -> ArticleSearchTag.COMMENT;
            default -> ArticleSearchTag.ALL;
        };
    }
}

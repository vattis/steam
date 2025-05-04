package com.example.steam.module.comment.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.article.domain.Article;
import com.example.steam.module.article.repository.ArticleRepository;
import com.example.steam.module.comment.domain.ArticleComment;
import com.example.steam.module.comment.domain.ProductComment;
import com.example.steam.module.comment.repository.ArticleCommentRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import com.example.steam.module.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    //댓글 달기
    public ArticleComment makeArticleComment(Long memberId, Long articleId, String content){
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(NoSuchElementException::new);
        ArticleComment articleComment = ArticleComment.of(member, article, content);
        return articleCommentRepository.save(articleComment);
    }

    //게시물 댓글 보기
    public Page<ArticleComment> findArticleCommentByArticleId(Long articleId, int pageNum){
        PageRequest pageRequest = PageRequest.of(pageNum, PageConst.ARTICLE_COMMENT_PAGE_SIZE);
        return articleCommentRepository.findAllByArticleId(articleId, pageRequest);
    }

    //댓글 삭제
    public boolean deleteArticleComment(Long articleCommentId, Long memberId){
        ArticleComment articleComment = articleCommentRepository.findById(articleCommentId).orElseThrow(NoSuchElementException::new);
        if(articleComment.getMember().getId() != memberId){
            log.info("잘못된 ArticleComment 삭제:: 회원 불일치");
            return false;
        }
        Article article = articleComment.getArticle();
        article.getComments().remove(articleComment);
        articleCommentRepository.delete(articleComment);
        return true;
    }
}

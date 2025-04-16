package com.example.steam.module.article.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.article.domain.Article;
import com.example.steam.module.article.dto.ArticleWriteForm;
import com.example.steam.module.article.repository.ArticleRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    //게시판 전체 탐색, page 로 반환, 날짜 최신순
    public Page<Article> findAllPage(){
        Pageable pageable = PageRequest.of(0, PageConst.ARTICLE_PAGE_SIZE);
        return articleRepository.findAllByOrderByCreated(pageable);
    }
    //단일 article 탐색
    public Article findArticle(Long id){
        return articleRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    //article 저장
    public Article saveArticle(ArticleWriteForm articleWriteForm){
        Member member = memberRepository.findById(articleWriteForm.getMemberDto().getId()).orElseThrow(NoSuchElementException::new);
        Article article = Article.of(member, articleWriteForm.getTitle(), articleWriteForm.getContent());
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id){
        articleRepository.deleteById(id);
    }

}

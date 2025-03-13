package com.example.steam.module.article.presentation;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.article.domain.Article;
import com.example.steam.module.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;

    //게시판 전체 탐색, page 로 반환, 날짜 최신순
    public Page<Article> findAllPage(){
        Pageable pageable = PageRequest.of(PageConst.ARTICLE_PAGE_NUMBER, PageConst.ARTICLE_PAGE_SIZE);
        return articleRepository.findAllOrderByCreatedAtDesc(pageable);
    }


}

package com.example.steam.module.article.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.article.domain.Article;
import com.example.steam.module.article.domain.ArticleSearch;
import com.example.steam.module.article.domain.ArticleSearchTag;
import com.example.steam.module.article.dto.ArticleWriteForm;
import com.example.steam.module.article.repository.ArticleRepository;
import com.example.steam.module.gallery.domain.Gallery;
import com.example.steam.module.gallery.repository.GalleryRepository;
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
    private final GalleryRepository galleryRepository;

    //게시판 통합 탐색, page 로 반환, 날짜 최신순
    public Page<Article> findAllPage(int pageNum){
        Pageable pageable = PageRequest.of(pageNum, PageConst.ARTICLE_PAGE_SIZE);
        return articleRepository.findAllByOrderByCreated(pageable);
    }

    //게시판 별 게시물 전체 찾기
    public Page<Article> findAllByGalleryId(Long galleryId, int pageNo){
        Pageable pageable = PageRequest.of(pageNo, PageConst.ARTICLE_PAGE_SIZE);
        return articleRepository.findAllByGalleryId(galleryId, pageable);
    }

    //게시판 별 검색
    public Page<Article> findAllBySearchWord(Long galleryId, ArticleSearch articleSearch){
        Pageable pageable = PageRequest.of(0, PageConst.ARTICLE_PAGE_SIZE);
        //Gallery gallery = galleryRepository.findById(galleryId).orElseThrow(NoSuchElementException::new);
        if(articleSearch.getTag() == ArticleSearchTag.ALL){
            return articleRepository.findAllByGalleryIdAndMemberNameOrContentOrTitleContaining(galleryId, articleSearch.getSearchWord(), pageable);
        }else if(articleSearch.getTag() == ArticleSearchTag.TITLE){
            return articleRepository.findAllByGalleryIdAndTitleContaining(galleryId, articleSearch.getSearchWord(), pageable);
        }else if(articleSearch.getTag() == ArticleSearchTag.CONTENT){
            return articleRepository.findAllByGalleryIdAndContentContaining(galleryId, articleSearch.getSearchWord(), pageable);
        }else if(articleSearch.getTag() == ArticleSearchTag.NICKNAME){
            return articleRepository.findAllByGalleryIdAndMemberNicknameContaining(galleryId, articleSearch.getSearchWord(), pageable);
        }else if(articleSearch.getTag() == ArticleSearchTag.COMMENT){
            return articleRepository.findAllByGalleryIdAndCommentsContentContaining(galleryId, articleSearch.getSearchWord(), pageable);
        }else {
            return null;
        }
    }


    //단일 article 통합 탐색
    public Article findArticle(Long id){
        return articleRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    //article 저장
    public Article saveArticle(ArticleWriteForm articleWriteForm, Member member){
        Gallery gallery = galleryRepository.findById(articleWriteForm.getGalleryId()).orElseThrow(NoSuchElementException::new);
        Article article = Article.of(gallery, member, articleWriteForm.getTitle(), articleWriteForm.getContent());
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id){
        articleRepository.deleteById(id);
    }

}

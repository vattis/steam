package com.example.steam.module.article.application;

import com.example.steam.module.article.domain.Article;
import com.example.steam.module.article.domain.ArticleSearch;
import com.example.steam.module.article.domain.ArticleSearchTag;
import com.example.steam.module.article.repository.ArticleRepository;
import com.example.steam.module.company.domain.Company;
import com.example.steam.module.gallery.domain.Gallery;
import com.example.steam.module.gallery.repository.GalleryRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import com.example.steam.module.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @InjectMocks ArticleService articleService;
    @Mock ArticleRepository articleRepository;
    @Mock MemberRepository memberRepository;
    @Mock GalleryRepository galleryRepository;

    @Test
    void findAllBySearchWordTest(){
        //given
        ArticleSearch titleArticleSearch = ArticleSearch.of(ArticleSearchTag.TITLE, "title1");
        ArticleSearch contentArticleSearch = ArticleSearch.of(ArticleSearchTag.CONTENT, "content1");
        ArticleSearch nicknameArticleSearch = ArticleSearch.of(ArticleSearchTag.NICKNAME, "nickname1");
        ArticleSearch commentArticleSearch = ArticleSearch.of(ArticleSearchTag.COMMENT, "content1");
        ArticleSearch allArticleSearch = ArticleSearch.of(ArticleSearchTag.ALL, "all1");
        Gallery gallery = Gallery.makeSample(Product.makeSample(1, Company.makeSample(1)));
        Member member = Member.makeSample(1);
        Long galleryId = 1L;
        List<Article> articles1 = new ArrayList<>();
        articles1.add(Article.of(gallery, member, "titleTest", "titleTest"));
        Page<Article> titleArticlePage = new PageImpl<>(articles1);

        List<Article> articles2 = new ArrayList<>();
        articles2.add(Article.of(gallery, member, "contentTest", "contentTest"));
        Page<Article> contentArticlePage = new PageImpl<>(articles2);

        List<Article> articles3 = new ArrayList<>();
        articles3.add(Article.of(gallery, member, "nicknameTest", "nicknameTest"));
        Page<Article> nicknameArticlePage = new PageImpl<>(articles3);

        List<Article> articles4 = new ArrayList<>();
        articles4.add(Article.of(gallery, member, "commentContentTest", "commentContentTest"));
        Page<Article> commentContentArticlePage = new PageImpl<>(articles4);

        List<Article> articles5 = new ArrayList<>();
        articles5.add(Article.of(gallery, member, "allTest", "allTest"));
        Page<Article> allArticlePage = new PageImpl<>(articles5);

        given(articleRepository.findAllByGalleryIdAndMemberNameOrContentOrTitleContaining(any(Long.class), any(String.class), any(PageRequest.class))).willReturn(allArticlePage);
        given(articleRepository.findAllByGalleryIdAndTitleContaining(any(Long.class), any(String.class), any(PageRequest.class))).willReturn(titleArticlePage);
        given(articleRepository.findAllByGalleryIdAndContentContaining(any(Long.class), any(String.class), any(PageRequest.class))).willReturn(contentArticlePage);
        given(articleRepository.findAllByGalleryIdAndMemberNicknameContaining(any(Long.class), any(String.class), any(PageRequest.class))).willReturn(nicknameArticlePage);
        given(articleRepository.findAllByGalleryIdAndCommentsContentContaining(any(Long.class), any(String.class), any(PageRequest.class))).willReturn(commentContentArticlePage);

        //when
        Page<Article> titleArticlePageResult = articleService.findAllBySearchWord(galleryId, titleArticleSearch);
        Page<Article> contentArticlePageResult = articleService.findAllBySearchWord(galleryId, contentArticleSearch);
        Page<Article> nicknameArticlePageResult = articleService.findAllBySearchWord(galleryId, nicknameArticleSearch);
        Page<Article> commentContentArticlePageResult = articleService.findAllBySearchWord(galleryId, commentArticleSearch);
        Page<Article> allArticlePageResult = articleService.findAllBySearchWord(galleryId, allArticleSearch);

        //then
        assertThat(titleArticlePageResult.toList().get(0).getTitle()).isEqualTo(articles1.get(0).getTitle());
        assertThat(contentArticlePageResult.toList().get(0).getTitle()).isEqualTo(articles2.get(0).getTitle());
        assertThat(nicknameArticlePageResult.toList().get(0).getTitle()).isEqualTo(articles3.get(0).getTitle());
        assertThat(commentContentArticlePageResult.toList().get(0).getTitle()).isEqualTo(articles4.get(0).getTitle());
        assertThat(allArticlePageResult.toList().get(0).getTitle()).isEqualTo(articles5.get(0).getTitle());

    }
}
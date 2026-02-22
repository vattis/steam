package com.example.steam.module.gallery.presentation;

import com.example.steam.module.article.application.ArticleService;
import com.example.steam.module.article.dto.ArticleDto;
import com.example.steam.module.gallery.application.GalleryService;
import com.example.steam.module.gallery.domain.Gallery;
import com.example.steam.module.gallery.domain.SimpleGalleryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class GalleryApiController {
    private final GalleryService galleryService;
    private final ArticleService articleService;

    // 갤러리 목록 조회
    @GetMapping("/galleries")
    public ResponseEntity<Page<SimpleGalleryDto>> getGalleries(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo) {

        Page<SimpleGalleryDto> galleries = galleryService.findAllGallery(pageNo).map(SimpleGalleryDto::from);
        return ResponseEntity.ok(galleries);
    }

    // 갤러리 상세 조회
    @GetMapping("/gallery/{galleryId}")
    public ResponseEntity<SimpleGalleryDto> getGallery(@PathVariable Long galleryId) {
        Gallery gallery = galleryService.findById(galleryId);
        return ResponseEntity.ok(SimpleGalleryDto.from(gallery));
    }

    // 갤러리 검색
    @GetMapping("/gallery/search")
    public ResponseEntity<Page<SimpleGalleryDto>> searchGalleries(
            @RequestParam("searchWord") String searchWord,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo) {

        Page<SimpleGalleryDto> galleries = galleryService.search(searchWord, pageNo).map(SimpleGalleryDto::from);
        return ResponseEntity.ok(galleries);
    }

    // 갤러리 게시글 목록 조회
    @GetMapping("/gallery/{galleryId}/articles")
    public ResponseEntity<Page<ArticleDto>> getGalleryArticles(
            @PathVariable Long galleryId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo) {

        Page<ArticleDto> articles = articleService.findAllByGalleryId(galleryId, pageNo).map(ArticleDto::from);
        return ResponseEntity.ok(articles);
    }
}

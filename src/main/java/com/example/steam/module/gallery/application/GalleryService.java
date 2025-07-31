package com.example.steam.module.gallery.application;

import com.example.steam.module.gallery.domain.Gallery;
import com.example.steam.module.gallery.repository.GalleryRepository;
import com.example.steam.module.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalleryService {
    private final GalleryRepository galleryRepository;

    //갤러리 생성
    public Gallery createGallery(Product product){
        Gallery gallery = Gallery.of(product);
        return galleryRepository.save(gallery);
    }

    //갤러리 전체 찾기
    public List<Gallery> findAllGallery(){
        return galleryRepository.findAll();
    }
}

package com.example.steam.module.gallery.repository;

import com.example.steam.module.gallery.domain.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    Page<Gallery> findByProduct_NameContaining(String searchWord, Pageable pageable);

    Optional<Gallery> findByProduct_Name(String productName);
}

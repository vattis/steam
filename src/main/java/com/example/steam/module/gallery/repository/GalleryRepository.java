package com.example.steam.module.gallery.repository;

import com.example.steam.module.gallery.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
}

package com.example.steam.module.comment.repository;

import com.example.steam.module.comment.domain.ProductComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {
}

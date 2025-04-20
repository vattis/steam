package com.example.steam.module.comment.repository;

import com.example.steam.module.comment.domain.ProfileComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileCommentRepository extends JpaRepository<ProfileComment, Long> {
    Page<ProfileComment> findAllByProfileMemberId(Long profileMemberId, PageRequest pageRequest);
}

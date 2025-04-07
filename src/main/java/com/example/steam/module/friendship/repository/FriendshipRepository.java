package com.example.steam.module.friendship.repository;

import com.example.steam.module.friendship.domain.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    public Page<Friendship> findAllByFromMemberId(Long fromMemberId, Pageable pageable);
    public boolean existsByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
    public Friendship findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
    public void deleteByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
}

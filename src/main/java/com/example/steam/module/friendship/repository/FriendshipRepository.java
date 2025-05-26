package com.example.steam.module.friendship.repository;

import com.example.steam.module.friendship.domain.Friendship;
import com.example.steam.module.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    public List<Friendship> findAllByFromMemberId(Long fromMemberId);
    public List<Friendship> findAllByToMemberAndAcceptedIsFalse(Member toMember);
    public List<Friendship> findAllByFromMemberAndAcceptedIsFalse(Member fromMember);
    public boolean existsByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
    public Friendship findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
    public void deleteByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
}

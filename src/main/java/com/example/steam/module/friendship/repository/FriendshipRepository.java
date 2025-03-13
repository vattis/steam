package com.example.steam.module.friendship.repository;

import com.example.steam.module.friendship.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
}

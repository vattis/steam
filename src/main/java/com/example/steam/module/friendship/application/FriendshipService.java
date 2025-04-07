package com.example.steam.module.friendship.application;

import com.example.steam.module.friendship.domain.Friendship;
import com.example.steam.module.friendship.repository.FriendshipRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    //해당 유저의 친구 관계 조회
    public Page<Friendship> getFriends(Long fromMemberId, Pageable pageable){
        return friendshipRepository.findAllByFromMemberId(fromMemberId, pageable);
    }

    //친구 신청
    public void addFriend(Long fromMemberId, Long toMemberId){ //
        if(friendshipRepository.existsByFromMemberIdAndToMemberId(fromMemberId, toMemberId)){ //이미 친구 신청을 했거나, 친구 관계인 경우
            if(friendshipRepository.findByFromMemberIdAndToMemberId(fromMemberId, toMemberId).getAccepted()){
                log.info("이미 친구 관계인 유저의 친구 신청 fromId:{} toId:{}", fromMemberId, toMemberId);
            }else{
                log.info("중복된 친구 신청 확인 fomId:{} toId:{}", fromMemberId, toMemberId);
            }
            return;
        }
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(NoSuchElementException::new); //친구 신청을 한 Member
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(NoSuchElementException::new); //친구 신청을 받은 Member
        Friendship friendship1 = Friendship.of(fromMember, toMember, false); //친구 관계1
        Friendship friendship2 = Friendship.createReverseFriendship(friendship1); //친구 관계2 (친구 관계1 역)
        fromMember.getFriendships().add(friendship1);
        toMember.getFriendships().add(friendship2);
        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
    }

    //친구 삭제
    public void removeFriendship(Long fromMemberId, Long toMemberId){
        friendshipRepository.deleteByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
        friendshipRepository.deleteByFromMemberIdAndToMemberId(toMemberId, fromMemberId);
    }
}

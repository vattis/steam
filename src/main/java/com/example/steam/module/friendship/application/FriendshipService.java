package com.example.steam.module.friendship.application;

import com.example.steam.module.friendship.domain.Friendship;
import com.example.steam.module.friendship.repository.FriendshipRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    //해당 유저의 친구 관계 조회
    public List<Friendship> getFriends(Long fromMemberId){
        return friendshipRepository.findAllByFromMemberId(fromMemberId);
    }

    //친구 신청
    public Friendship inviteFriend(Long fromMemberId, Long toMemberId){ //
        if(friendshipRepository.existsByFromMemberIdAndToMemberId(fromMemberId, toMemberId)){ //이미 친구 신청을 했거나, 친구 관계인 경우
            if(friendshipRepository.findByFromMemberIdAndToMemberId(fromMemberId, toMemberId).getAccepted()){
                log.info("이미 친구 관계인 유저의 친구 신청 fromId:{} toId:{}", fromMemberId, toMemberId);
            }else{
                log.info("중복된 친구 신청 확인 fomId:{} toId:{}", fromMemberId, toMemberId);
            }
            return null;
        }
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(NoSuchElementException::new); //친구 신청을 한 Member
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(NoSuchElementException::new); //친구 신청을 받은 Member
        Friendship friendship1 = Friendship.of(fromMember, toMember, false); //친구 관계
        fromMember.getFriendships().add(friendship1);
        return friendshipRepository.save(friendship1);
    }

    //친구 수락
    public void acceptFriend(Long fromMemberId, Long toMemberId){
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(NoSuchElementException::new);
        Member fromMember = memberRepository.findById(fromMemberId).orElseThrow(NoSuchElementException::new);
        Friendship friendship = friendshipRepository.findByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
        if(friendship.getAccepted()){
            log.info("이미 수락된 친구 관계 수락 요청  fromId:{} toId:{}", fromMemberId, toMemberId);
            return;
        }
        Friendship friendship1 = friendship.acceptFriendship();
        Friendship friendship2 = Friendship.createReverseFriendship(friendship1);
        friendshipRepository.save(friendship2);
        toMember.getFriendships().add(friendship2);
    }

    //초대 받은 친구 요청 확인
    public List<Friendship> getFriendRequest(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        return friendshipRepository.findAllByToMemberAndAcceptedIsFalse(member);
    }

    //초대한 친구 요청 확인
    public List<Friendship> getFriendInvitation(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        return friendshipRepository.findAllByFromMemberAndAcceptedIsFalse(member);
    }


    //친구 삭제
    public void removeFriendship(Long fromMemberId, Long toMemberId){
        friendshipRepository.deleteByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
        friendshipRepository.deleteByFromMemberIdAndToMemberId(toMemberId, fromMemberId);
    }
}

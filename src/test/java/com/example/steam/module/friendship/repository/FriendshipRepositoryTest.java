package com.example.steam.module.friendship.repository;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.friendship.domain.Friendship;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FriendshipRepositoryTest {
    @Autowired private FriendshipRepository friendshipRepository;
    @Autowired private MemberRepository memberRepository;
    private List<Member> members;
    private List<Friendship> friendships;

    @BeforeEach
    void init(){
        List<Member> members = new ArrayList<>();
        List<Friendship> friendships = new ArrayList<>();
        for(int i = 1; i <= 10; i++){
            members.add(memberRepository.save(Member.makeSample(i)));
        }
        memberRepository.saveAll(members);
        for(int i = 0; i <= 8; i++){
            Friendship friendship1 = Friendship.of(members.get(i), members.get(i+1), true);
            Friendship friendship2 = Friendship.createReverseFriendship(friendship1);
            friendships.add(friendship1);
            friendships.add(friendship2);
        }
        for(int i = 0; i <= 7; i++){
            Friendship friendship1 = Friendship.of(members.get(i), members.get(i+2), true);
            Friendship friendship2 = Friendship.createReverseFriendship(friendship1);
            friendships.add(friendship1);
            friendships.add(friendship2);
        }
        friendshipRepository.saveAll(friendships);
        this.members = members;
        this.friendships = friendships;
    }

    @Test
    void findAllByFromMemberId() {
        //given
        PageRequest pageable = PageRequest.of(0, PageConst.FRIENDS_PAGE_SIZE);

        //when
        Page<Friendship> friendshipPage1 = friendshipRepository.findAllByFromMemberId(members.get(0).getId(), pageable);

        //then
        assertThat(friendshipPage1.getTotalElements()).isEqualTo(2);
        friendshipPage1.forEach(a->assertThat(a.getFromMember()).isEqualTo(members.get(0)));
    }

    @Test
    void existsByFromMemberIdAndToMemberId() {
        //given
        Member toMember1 = members.get(5);
        Member fromMember1 = members.get(6);

        Member toMember2 = members.get(1);
        Member fromMember2 = members.get(7);

        //when
        Boolean resultTrue = friendshipRepository.existsByFromMemberIdAndToMemberId(fromMember1.getId(), toMember1.getId());
        Boolean resultFalse = friendshipRepository.existsByFromMemberIdAndToMemberId(fromMember2.getId(), toMember2.getId());

        //then
        assertThat(resultTrue).isTrue();
        assertThat(resultFalse).isFalse();
    }

    @Test
    void findByFromMemberIdAndToMemberId() {
        //given
        Member fromMember1 = members.get(5);
        Member toMember1 = members.get(6);

        //when
        Friendship friendship = friendshipRepository.findByFromMemberIdAndToMemberId(fromMember1.getId(), toMember1.getId());

        //then
        assertThat(friendship.getFromMember()).isEqualTo(fromMember1);
        assertThat(friendship.getToMember()).isEqualTo(toMember1);
    }

    @Test
    void deleteByFromMemberIdAndToMemberId() {
        //given
        Member fromMember1 = members.get(5);
        Member toMember1 = members.get(6);

        //when
        friendshipRepository.deleteByFromMemberIdAndToMemberId(fromMember1.getId(), toMember1.getId());
        Boolean result = friendshipRepository.existsByFromMemberIdAndToMemberId(fromMember1.getId(), toMember1.getId());

        //then
        assertThat(result).isFalse();
    }
}
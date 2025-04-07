package com.example.steam.module.friendship.application;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.friendship.domain.Friendship;
import com.example.steam.module.friendship.repository.FriendshipRepository;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {
    @InjectMocks FriendshipService friendshipService;
    @Mock FriendshipRepository friendshipRepository;
    @Mock MemberRepository memberRepository;

    @Test
    void getFriends() {
        //given
        PageRequest pageRequest = PageRequest.of(0, PageConst.FRIENDS_PAGE_SIZE);
        Page<Friendship> friendshipPage = new PageImpl<Friendship>(new ArrayList<Friendship>());
        given(friendshipRepository.findAllByFromMemberId(1L, pageRequest)).willReturn(friendshipPage);

        //when
        Page<Friendship> friendshipPageResult = friendshipService.getFriends(1L, pageRequest);

        //then
        assertThat(friendshipPageResult).isEqualTo(friendshipPage);
    }

    @Test
    void addFriend1() {
        //given
        Member friendMember1 = Member.makeSample(1);
        Member friendMember2 = Member.makeSample(2);
        ReflectionTestUtils.setField(friendMember1, "id", 1L);
        ReflectionTestUtils.setField(friendMember2, "id", 2L);



        given(friendshipRepository.existsByFromMemberIdAndToMemberId(friendMember1.getId(), friendMember2.getId())).willReturn(true);


        given(friendshipRepository.findByFromMemberIdAndToMemberId(friendMember1.getId(), friendMember2.getId())).willReturn(Friendship.of(friendMember1, friendMember2, true));

        //when
        friendshipService.addFriend(friendMember1.getId(), friendMember2.getId());

        //then
        verify(friendshipRepository, never()).save(any(Friendship.class));

    }

    @Test
    void addFriend2(){
        //given
        Member nonFriendMember1 = Member.makeSample(3);
        Member nonFriendMember2 = Member.makeSample(4);
        ReflectionTestUtils.setField(nonFriendMember1, "id", 3L);
        ReflectionTestUtils.setField(nonFriendMember2, "id", 4L);

        given(friendshipRepository.existsByFromMemberIdAndToMemberId(nonFriendMember1.getId(), nonFriendMember2.getId())).willReturn(false);
        given(memberRepository.findById(nonFriendMember1.getId())).willReturn(Optional.of(nonFriendMember1));
        given(memberRepository.findById(nonFriendMember2.getId())).willReturn(Optional.of(nonFriendMember2));

        //when
        friendshipService.addFriend(nonFriendMember1.getId(), nonFriendMember2.getId());

        //then
        verify(friendshipRepository, times(2)).save(any(Friendship.class));

    }

    @Test
    void removeFriendship() {
        //given
        Member friendMember1 = Member.makeSample(1);
        Member friendMember2 = Member.makeSample(2);
        ReflectionTestUtils.setField(friendMember1, "id", 1L);
        ReflectionTestUtils.setField(friendMember2, "id", 2L);

        //when
        friendshipService.removeFriendship(friendMember1.getId(), friendMember2.getId());

        //then
        verify(friendshipRepository, times(1)).deleteByFromMemberIdAndToMemberId(friendMember1.getId(), friendMember2.getId());
        verify(friendshipRepository, times(1)).deleteByFromMemberIdAndToMemberId(friendMember2.getId(), friendMember1.getId());
    }
}
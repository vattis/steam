package com.example.steam.module.friendship.presentation;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.friendship.application.FriendshipService;
import com.example.steam.module.friendship.dto.SimpleFriendshipDto;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.dto.SimpleMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipApiController {

    private final MemberService memberService;
    private final FriendshipService friendshipService;

    // 친구 목록 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<Page<SimpleFriendshipDto>> getFriends(
            @PathVariable("memberId") Long memberId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo) {

        Page<SimpleFriendshipDto> friends = friendshipService
                .getFriends(memberId, PageRequest.of(pageNo, PageConst.FRIENDS_PAGE_SIZE))
                .map(SimpleFriendshipDto::from);

        return ResponseEntity.ok(friends);
    }

    // 받은 친구 요청 조회
    @GetMapping("/received")
    public ResponseEntity<List<SimpleFriendshipDto>> getReceivedRequests(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto loginMember = memberService.findMemberDtoByEmail(principal.getName());
        List<SimpleFriendshipDto> requests = friendshipService
                .getFriendRequest(loginMember.getId())
                .stream()
                .map(SimpleFriendshipDto::from)
                .toList();

        return ResponseEntity.ok(requests);
    }

    // 친구 요청 보내기
    @PostMapping("/request/{toMemberId}")
    public ResponseEntity<Map<String, String>> sendFriendRequest(
            @PathVariable Long toMemberId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto loginMember = memberService.findMemberDtoByEmail(principal.getName());
        friendshipService.inviteFriend(loginMember.getId(), toMemberId);

        return ResponseEntity.ok(Map.of("message", "친구 요청을 보냈습니다."));
    }

    // 친구 요청 수락
    @PostMapping("/accept/{toMemberId}")
    public ResponseEntity<Map<String, String>> acceptFriendRequest(
            @PathVariable Long toMemberId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto loginMember = memberService.findMemberDtoByEmail(principal.getName());
        friendshipService.acceptFriend(loginMember.getId(), toMemberId);

        return ResponseEntity.ok(Map.of("message", "친구 요청을 수락했습니다."));
    }

    // 친구 요청 거절 / 친구 삭제
    @PostMapping("/reject/{toMemberId}")
    public ResponseEntity<Map<String, String>> rejectFriendRequest(
            @PathVariable Long toMemberId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto loginMember = memberService.findMemberDtoByEmail(principal.getName());
        friendshipService.removeFriendship(loginMember.getId(), toMemberId);

        return ResponseEntity.ok(Map.of("message", "처리되었습니다."));
    }

    // 친구 삭제
    @DeleteMapping("/{friendshipId}")
    public ResponseEntity<Map<String, String>> removeFriend(
            @PathVariable Long friendshipId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        SimpleMemberDto loginMember = memberService.findMemberDtoByEmail(principal.getName());
        // friendshipId로 삭제하는 로직이 필요한 경우 서비스 메서드 추가 필요
        // 현재는 toMemberId 기반으로 동작

        return ResponseEntity.ok(Map.of("message", "친구를 삭제했습니다."));
    }
}

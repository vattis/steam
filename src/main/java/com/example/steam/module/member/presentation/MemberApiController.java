package com.example.steam.module.member.presentation;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.member.application.MemberGameService;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.dto.MemberGameDto;
import com.example.steam.module.member.dto.MemberSearch;
import com.example.steam.module.member.dto.ProfileDto;
import com.example.steam.module.member.dto.SimpleMemberDto;
import com.example.steam.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
    private final MemberService memberService;
    private final MemberGameService memberGameService;
    private final MemberRepository memberRepository;

    @GetMapping("/profile/{memberId}")
    public ResponseEntity<ProfileDto> getProfile(
            @PathVariable("memberId") Long memberId,
            @RequestParam(name = "commentPageNum", required = false, defaultValue = "0") int commentPageNum) {

        ProfileDto profileDto = memberService.getProfile(memberId, PageRequest.of(commentPageNum, PageConst.PROFILE_COMMENT_PAGE_SIZE));
        return ResponseEntity.ok(profileDto);
    }

    @GetMapping("/library/{memberId}")
    public ResponseEntity<List<MemberGameDto>> getLibrary(
            @PathVariable("memberId") Long memberId,
            Principal principal) {

        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);

        // 본인 라이브러리만 조회 가능
        if (principal == null || !member.getEmail().equals(principal.getName())) {
            return ResponseEntity.status(403).build();
        }

        List<MemberGameDto> games = memberGameService.getMemberGameDtosByMember(member);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/library/{memberId}/{productId}")
    public ResponseEntity<MemberGameDto> getLibraryGame(
            @PathVariable("memberId") Long memberId,
            @PathVariable("productId") Long productId,
            Principal principal) {

        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);

        // 본인 라이브러리만 조회 가능
        if (principal == null || !member.getEmail().equals(principal.getName())) {
            return ResponseEntity.status(403).build();
        }

        if (!memberGameService.isOwned(member, productId)) {
            return ResponseEntity.notFound().build();
        }

        MemberGameDto game = memberGameService.findDtoByMemberAndProductId(member, productId);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/members")
    public ResponseEntity<Page<SimpleMemberDto>> searchMembers(
            @RequestParam(name = "searchTag", defaultValue = "nickname") String searchTag,
            @RequestParam(name = "searchWord", defaultValue = "") String searchWord,
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            Principal principal) {

        Page<SimpleMemberDto> simpleMemberDtos = memberService
                .searchMember(MemberSearch.of(searchTag, searchWord), pageNo, principal)
                .map(SimpleMemberDto::from);

        Page<SimpleMemberDto> result = memberService.attachFriendState(simpleMemberDtos, principal);
        return ResponseEntity.ok(result);
    }
}

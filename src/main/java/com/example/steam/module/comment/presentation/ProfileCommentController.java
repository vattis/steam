package com.example.steam.module.comment.presentation;

import com.example.steam.module.comment.application.ProfileCommentService;
import com.example.steam.module.comment.domain.ProfileComment;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProfileCommentController {
    private final ProfileCommentService profileCommentService;
    private final MemberService memberService;

    @PostMapping("/profile/comment")
    public String makeProfileComment(
            @RequestParam("memberId") Long memberId,
            @RequestParam("profileId") Long profileId,
            @RequestParam("content") String content) {
        profileCommentService.makeProfileComment(memberId, profileId, content);
        return "redirect:/profile/" + profileId + "/0";
    }
}

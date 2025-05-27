package com.example.steam.module.friendship.presentation;

import com.example.steam.module.friendship.application.FriendshipService;
import com.example.steam.module.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final MemberService memberService;

    @GetMapping("/friendship/{memberId}")
    public String friendship(@PathVariable("memberId") Long memberId, Model model) {
        //friendshipService.getFriends(memberId, )
        return ";";
    }
}

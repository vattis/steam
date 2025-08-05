package com.example.steam.module.member.presentation;

import com.example.steam.core.utils.page.PageConst;
import com.example.steam.module.email.application.EmailService;
import com.example.steam.module.member.application.MemberService;
import com.example.steam.module.member.domain.Member;
import com.example.steam.module.member.domain.MemberGame;
import com.example.steam.module.member.dto.MemberGameDto;
import com.example.steam.module.member.dto.ProfileDto;
import com.example.steam.module.member.repository.MemberGameRepository;
import com.example.steam.module.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final MemberGameRepository memberGameRepository;
    private int tempAuthNum; //임시 인증 번호 저장소 이후에 redis에 저장 예정

    @ResponseBody
    @PostMapping("/auth/sendEmail/{email}") //사용자 이메일 인증 이메일 전송
    public int sendAuthEmail(@PathVariable("email") String email) {
        int authNum = memberService.createAuthCode();
        String body = "";
        body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
        body += "<h1>" + authNum + "</h1>";
        body += "<h3>" + "감사합니다." + "</h3>";
        try {
            emailService.sendEmail(email, "steam 인증입니다.", body);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        tempAuthNum = authNum;
        return authNum;
    }

    @GetMapping("/auth/check/{email}/{authCode}") //사용자 이메일 인증 숫자 확인
    @ResponseBody
    public ResponseEntity<?> checkAuth(@RequestParam("authCode") String authNum) {
        return ResponseEntity.ok(tempAuthNum == Integer.parseInt(authNum));
    }

    @GetMapping({"/library/{memberId}", "/library/{memberId}/{selectedGameId}"}) //유저 라이브러리
    public String gotoLibrary(@PathVariable("memberId") Long memberId, @PathVariable(value = "selectedGameId", required = false) Long selectedGameId, Model model, Principal principal) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        if(member.getEmail() != principal.getName()){ //다른 사용자의 접근 차단
            log.info("[MemberController] 일치하지 않은 유저의 라이브러리 접근 확인");
            return "redirect:/";
        }
        model.addAttribute("games", member.getMemberGames().stream().map(MemberGameDto::from).toList());
        if(selectedGameId == null){
            model.addAttribute("selectedGame", null);
        }else{
            MemberGame selectedGame = memberGameRepository.findById(selectedGameId).orElseThrow(NoSuchElementException::new);
            model.addAttribute("selectedGame", MemberGameDto.from(selectedGame));
        }
        return "/library";
    }

    //유저 프로필 이동
    @GetMapping("/profile/{memberId}/{commentPageNum}")
    public String gotoMyPage(@PathVariable Long memberId, @PathVariable int commentPageNum, Model model) {
        ProfileDto profileDto = memberService.getProfile(memberId, PageRequest.of(commentPageNum, PageConst.PROFILE_COMMENT_PAGE_SIZE));
        model.addAttribute("profileDto", profileDto);
        return "/profile";
    }
}

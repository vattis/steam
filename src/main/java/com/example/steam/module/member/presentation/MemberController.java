package com.example.steam.module.member.presentation;

import com.example.steam.module.email.application.EmailService;
import com.example.steam.module.member.application.MemberService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
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

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "index";
    }
}

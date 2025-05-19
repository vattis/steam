package com.example.steam.module.login.presentation;

import com.example.steam.core.jwt.JwtConst;
import com.example.steam.core.jwt.JwtToken;
import com.example.steam.module.login.application.LoginService;
import com.example.steam.module.login.dto.LoginForm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @GetMapping("/login")
    public String gotoLogin(Model model){
        model.addAttribute("loginForm", LoginForm.of());
        return "login";
    }

    @PostMapping("/sign-in")
    public String login(@ModelAttribute LoginForm loginForm, HttpServletResponse response) {
        log.info("[LoginController]-login  " + "email: " + loginForm.getEmail() + " password: " + loginForm.getPassword());
        JwtToken token = loginService.login(loginForm);
        log.info("accessToken: " + token.getAccessToken());
        log.info("refreshToken: " + token.getRefreshToken());

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", token.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtConst.JWT_ACCESS_TOKEN_EXPIRES_IN)
                .sameSite("Lax")
                .build();

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtConst.JWT_REFRESH_TOKEN_EXPIRES_IN)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return "redirect:/";
    }
    @PostMapping("login/test")
    public String test(){
        return "/main";
    }
}

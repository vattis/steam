package com.example.steam.module.login.presentation;

import com.example.steam.core.jwt.JwtToken;
import com.example.steam.module.login.application.LoginService;
import com.example.steam.module.login.dto.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/login")
    public JwtToken login(@RequestBody LoginForm loginForm) {
        log.info("email: " + loginForm.getEmail() + " password: " + loginForm.getPassword());
        JwtToken token = loginService.login(loginForm);
        log.info("accessToken: " + token.getAccessToken());
        log.info("refreshToken: " + token.getRefreshToken());
        return token;
    }
    @PostMapping("login/test")
    public String test(){
        return "success";
    }
}

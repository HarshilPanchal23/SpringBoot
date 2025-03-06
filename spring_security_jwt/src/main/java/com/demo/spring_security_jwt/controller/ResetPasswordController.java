package com.demo.spring_security_jwt.controller;

import com.demo.spring_security_jwt.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final LoginService loginService;

    @GetMapping("/reset-password")
    public ModelAndView showResetPasswordPage(@RequestParam("token") String token) {
        ModelAndView modelAndView = new ModelAndView("ResetPasswordFile");
        modelAndView.addObject("token", token);
        return modelAndView;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        return loginService.resetPassword(token, newPassword);
    }
}

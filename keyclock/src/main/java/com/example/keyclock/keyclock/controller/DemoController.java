package com.example.keyclock.keyclock.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {


    @GetMapping("hello-1")
    @PreAuthorize("hasRole('client_user')")
    public String hello1() {
        return "hello from 1 ";
    }

    @GetMapping("hello-2")
    @PreAuthorize("hasRole('client_admin')")
    public String hello2() {
        return "hello from 2 -  Admin";
    }

}

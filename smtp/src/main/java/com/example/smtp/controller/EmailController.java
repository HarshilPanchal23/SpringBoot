package com.example.smtp.controller;

import com.example.smtp.entity.EmailDetails;
import com.example.smtp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody EmailDetails request) {
        // Handle mail sending logic here
        emailService.sendSimpleMail(request);
        return ResponseEntity.ok("Mail sent successfully");
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetails details) {
        String status = emailService.sendMailWithAttachment(details);
        System.out.println("status = " + status);
        return status;
    }
}
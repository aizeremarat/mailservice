package com.example.mailservice.controller;

import com.example.mailservice.entity.MailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.mailservice.service.impl.MailService;
import org.springframework.web.multipart.MultipartFile;

@RestController

public class MailController {
    private static MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        MailController.mailService = mailService;
    }

//    @PostMapping(value = "/mail")
//    public static ResponseEntity<String> sendMail(@RequestBody MailRequest mailRequest) {
//        MailController.sendMail(mailRequest);
//        return ResponseEntity.ok("Email sent successfully!");
//    }


    @GetMapping(value = "/mail")
    public static ResponseEntity<String> sendMail() {
        mailService.sendMail("marataizere22@gmail.com", "Message", "Hello");
        return ResponseEntity.ok("Email sent successfully!");
    }


}


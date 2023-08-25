package com.example.mailservice.controller;

import com.example.mailservice.service.MailService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MailController {
    public MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String sendEmailWithAttachments(
            @RequestPart(value = "file", required = false) MultipartFile[] files,
            @RequestPart(value = "to") String to,
            @RequestPart(value = "subject") String subject,
            @RequestPart(value = "body") String body,
            @RequestPart(value = "addressType") String addressType) {

        StringBuilder mailContent = new StringBuilder();
        mailContent.append("<html><body>");
        mailContent.append(body);
        mailContent.append("</body></html>");

        if (!mailService.isValidEmail(to)) {
            return "Address not valid";
        }

        boolean emailsent = mailService.sendEmailWithAttachments(
                files,
                to,
                subject,
                mailContent.toString(),
                addressType
        );
        if (emailsent) {
            return "Email sent successfully!";
        } else {
            return "Failed to send email.";
        }
    }
}






package com.example.mailservice.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendEmailWithAttachments(MultipartFile[] files, String to, String subject, String body, String addressType) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            String[] internalDomains = {"gmail.com"};
            String recipientDomain = to.substring(to.lastIndexOf("@") + 1);
            if (Arrays.asList(internalDomains).contains(recipientDomain)) {
                addressType = "internal";
            } else {
                addressType = "external";
            }

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            long totalFileSize = 0;

            for (MultipartFile file : files) {
                if (!Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                    long fileSize = file.getSize();
                    if (fileSize <= 25 * 1024 * 1024) {
                        totalFileSize += fileSize;
                        if (totalFileSize <= 25 * 1024 * 1024) {
                            String fileName = file.getOriginalFilename();
                            ByteArrayResource resource = new ByteArrayResource(file.getBytes());
                            helper.addAttachment(fileName, resource);
                        }
                    }
                }
            }
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
}

    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(gmail.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}






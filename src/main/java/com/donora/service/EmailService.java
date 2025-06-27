package com.donora.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
//EmailService ➡️ I am , jo letter likhte ho aur postman ko dete ho.
public class EmailService {
    //JavaMailSender ➡️ Postman hai, jo letter le jaata hai.
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        //SimpleMailMessage ➡️ Letter hai, jisme likha hota hai: kisko bhejna hai, kya likha hai, kaun bhej raha hai.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("immu70391@gmail.com"); //sender
        message.setTo(to); // receiver
        message.setSubject(subject); //subject of email
        message.setText(body);  // message of email
        mailSender.send(message);
    }
}

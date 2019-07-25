package com.kechun.util;


import com.kechun.scheduled.ProcessClearStream;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@ToString
@Getter
@Setter
public class EmailSender implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessClearStream.class);

    private String[] toUserArr;
    private String toUser;
    private String ccUser;
    private String[] ccUserArr;
    private String subject;
    private String content;
    private String fromUser;
    private JavaMailSenderImpl javaMailSender;

    public EmailSender(JavaMailSenderImpl javaMailSender, String ccUser, String toUser, String fromUser, String content, String subject) {
        this.toUser = toUser;
        this.ccUser = ccUser;
        this.subject = subject;
        this.content = content;
        this.fromUser = fromUser;
        this.javaMailSender = javaMailSender;
    }

    private MimeMessage SetMimeMessage(){
        MimeMessage mailMessage = null;
        try {
            mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setTo(toUser);
            messageHelper.setCc(ccUser);
            messageHelper.setFrom(fromUser);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mailMessage;
    }


    public void run(){
        MimeMessage mailMessage = SetMimeMessage();
        javaMailSender.send(mailMessage);
    }
}

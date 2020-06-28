package com.minsheng.reinsurance.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 * Created by panwei on 2017/6/16.
 */
@Service
public class SendEmailUtil {
    private static Logger logger = Logger.getLogger(SendEmailUtil.class);
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String subject, String from, String[] to, String[] cc, String content, boolean html) throws Exception {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, false, "utf-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setCc(cc);
        helper.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
        helper.setText(content, html);
        javaMailSender.send(msg);
    }

}

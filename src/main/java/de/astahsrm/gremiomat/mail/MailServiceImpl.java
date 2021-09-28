package de.astahsrm.gremiomat.mail;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendMail(String to) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", "Jon Doe");
        context.setVariable("username", "joDoe");
        String process = templateEngine.process("mail/test", context);
        javax.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Test");
        helper.setText(process, true);
        helper.setTo(to);
        mailSender.send(mimeMessage);
    }
}

package de.astahsrm.gremiomat.mail;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.security.MgmtUser;

@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendResetPasswordMail(String appUrl, Locale locale, String token, MgmtUser user) {
        String url = appUrl + "/user/change-password?token=" + token;
        Context context = new Context();
        Candidate details = user.getDetails();
        context.setVariable("name", details.getFirstname() + " " + details.getLastname());
        context.setVariable("username", user.getUsername());
        context.setVariable("url", url);
        context.setLocale(locale);
        try {
            send(templateEngine.process("mail/reset", context), user);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void send(String html, MgmtUser toUser) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Test");
        helper.setText(html, true);
        helper.setTo(toUser.getDetails().getEmail());
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendWelcomeMail(Locale locale, MgmtUser user) {
        Context context = new Context();
        Candidate details = user.getDetails();
        context.setVariable("name", details.getFirstname() + " " + details.getLastname());
        context.setVariable("username", user.getUsername());
        context.setLocale(locale);
        try {
            send(templateEngine.process("mail/welcome", context), user);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

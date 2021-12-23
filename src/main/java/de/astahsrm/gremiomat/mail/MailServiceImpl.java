package de.astahsrm.gremiomat.mail;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import de.astahsrm.gremiomat.mgmt.MgmtUser;
import de.astahsrm.gremiomat.mgmt.MgmtUserService;

@Service
public class MailServiceImpl implements MailService {

    @Value("${application.hostname}")
    private String hostname;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MgmtUserService mgmtUserService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void sendResetPasswordMail(Locale locale, MgmtUser user) throws MessagingException {
        Context context = new Context();
        String url = hostname + "/change-password?token=" + mgmtUserService.createPasswordResetTokenForUser(user);
        context.setVariable("url", url);
        context.setLocale(locale);
        send(messageSource.getMessage("mail.reset.subject", null, locale),
                templateEngine.process("mail/reset", context), user.getEmail().toLowerCase());
    }

    @Override
    public void sendWelcomeMail(Locale locale, MgmtUser user) throws MessagingException {
        Context context = new Context();
        String url = hostname + "/change-password?token=" + mgmtUserService.createPasswordResetTokenForUser(user);
        context.setVariable("user", user);
        context.setVariable("url", url);
        context.setLocale(locale);
        send(messageSource.getMessage("mail.welcome.title", null, locale),
                templateEngine.process("mail/welcome", context), user.getEmail().toLowerCase());
    }

    private void send(String subject, String html, String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setText(html, true);
        helper.setTo(email);
        helper.setFrom(from);
        mailSender.send(mimeMessage);
    }
}

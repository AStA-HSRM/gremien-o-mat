package de.astahsrm.gremiomat.mail;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import de.astahsrm.gremiomat.candidate.Candidate;
import de.astahsrm.gremiomat.mgmt.MgmtUser;
import de.astahsrm.gremiomat.mgmt.MgmtUserService;

@Service
public class MailServiceImpl implements MailService {

    public static final String URL = "localhost:8090/user/change-password?token=";

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
    public void sendResetPasswordMail(Locale locale, MgmtUser user) throws NoSuchMessageException, MessagingException {
        Context context = new Context();
        String url = URL + mgmtUserService.createPasswordResetTokenForUser(user);
        context.setVariable("url", url);
        context.setLocale(locale);
        send(messageSource.getMessage("mail.reset.subject", null, locale),
                templateEngine.process("mail/reset", context), user);
    }

    @Override
    public void sendWelcomeMail(Locale locale, MgmtUser user) throws NoSuchMessageException, MessagingException {
        Context context = new Context();
        Candidate details = user.getDetails();
        String url = URL + mgmtUserService.createPasswordResetTokenForUser(user);
        context.setVariable("fullname", details.getFirstname() + " " + details.getLastname());
        context.setVariable("username", user.getUsername());
        context.setVariable("url", url);
        context.setLocale(locale);
        send(messageSource.getMessage("mail.welcome.title", null, locale),
                templateEngine.process("mail/welcome", context), user);
    }

    @Override
    public void sendAdminWelcomeMail(Locale locale, MgmtUser user, String firstname, String lastname, String email)
            throws NoSuchMessageException, MessagingException {
        Context context = new Context();
        String url = URL + mgmtUserService.createPasswordResetTokenForUser(user);
        context.setVariable("fullname", firstname + " " + lastname);
        context.setVariable("username", user.getUsername());
        context.setVariable("url", url);
        context.setLocale(locale);
        send(messageSource.getMessage("mail.welcome.title", null, locale),
                templateEngine.process("mail/welcome", context), user);
    }

    private void send(String subject, String html, MgmtUser toUser) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setText(html, true);
        helper.setTo(toUser.getDetails().getEmail());
        mailSender.send(mimeMessage);
    }
}

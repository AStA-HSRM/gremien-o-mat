package de.astahsrm.gremiomat.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import de.astahsrm.gremiomat.candidate.Candidate;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender emailSender;

    // read out mail username from application.properties
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Override
    public void sendWelcomeMailToCandidate(Candidate candidate, String plainPassword) {
        /*
         * @Autowired public SimpleMailMessage template;
         * 
         * String text = String.format(template.getText(), templateArgs);
         * sendSimpleMessage(to, subject, text);
         */

        Context context = new Context();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(candidate.getEmail());
        message.setSubject("Example subject");
        message.setText(thymeleafTemplateEngine.process("mail/test", context));
        emailSender.send(message);
    }
}

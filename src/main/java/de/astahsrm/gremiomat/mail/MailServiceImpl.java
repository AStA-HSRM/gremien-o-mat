package de.astahsrm.gremiomat.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
    public void sendWelcomeMailToCandidate(Candidate candidate, String password) {
        /*
         * @Autowired public SimpleMailMessage template;
         * 
         * String text = String.format(template.getText(), templateArgs);
         * sendSimpleMessage(to, subject, text);
         */

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(candidate.getEmail());
        message.setSubject("Example subject");
        message.setText("Example text");
        emailSender.send(message);
    }
}
